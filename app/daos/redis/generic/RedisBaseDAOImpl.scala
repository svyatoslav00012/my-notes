package daos.redis.generic

import daos.ModelDAO
import models.HasId
import monix.eval.Task
import play.api.libs.json.{Json, OFormat}
import services.Cache


abstract class RedisBaseDAOImpl[T <: HasId[ID] : OFormat, ID](modelName: String, cache: Cache) extends ModelDAO[T, ID] {

  override def getAll: Task[List[T]] = {
    cache.get(modelName)
      .map(_.getOrElse("[]"))
      .map(Json.parse)
      .map(_.as[List[T]])
  }

  override def getById(id: ID): Task[Option[T]] = getAll.map(all => all.find(_.id == id))

  override def create(model: T): Task[T] = mutate { old =>
    val exists = old.exists(_.id == model.id)
    if (exists)
      throw new Exception("Already Exists")
    old :+ model
  }.map(_ => model)

  override def update(model: T): Task[Unit] = mutate { old =>
    val exists = old.exists(_.id == model.id)
    if (!exists)
      throw new Exception("Not Exists")
    old.map {
      case m if m.id == model.id => model
      case other => other
    }
  }.map(_ => Unit)

  override def delete(id: ID): Task[Unit] = mutate { old =>
    val exists = old.exists(_.id == id)
    if (!exists)
      throw new Exception("Not Exists")
    old.filter(_.id != id)
  }.map(_ => Unit)

  def mutate(mutation: List[T] => List[T]): Task[Boolean] =
    for {
      all <- getAll
      newAll = mutation(all)
      saved <- saveAll(newAll)
    } yield saved


  def saveAll(all: List[T]): Task[Boolean] = cache.set(modelName, Json.toJson(all).toString)
}
