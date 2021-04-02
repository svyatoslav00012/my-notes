package daos.mongo.generic

import controllers.utils.Exceptions.DbResultException
import daos.ModelDAO
import models.HasId
import monix.eval.Task
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.bson.collection.BSONSerializationPack.{Reader, Writer}
import reactivemongo.api.bson.{BSONDocument, ElementProducer}

import java.util.UUID
import scala.concurrent.ExecutionContext
import daos.helpers.Helpers._

abstract class MongoBaseDAOImpl[T <: HasId[UUID] : Reader : Writer](reactiveMongoApi: ReactiveMongoApi,
                                                                collectionName: String)
                                                               (implicit ex: ExecutionContext) extends ModelDAO[T, UUID] {

  import reactivemongo.play.json.compat
  import compat.json2bson._

  implicit val collection: Task[BSONCollection] = reactiveMongoApi.database.map(_.collection(collectionName)).wrapEx

  override def getById(id: UUID): Task[Option[T]] = collection.flatMap {
    _.find(BSONDocument("_id" -> id)).one[T].wrapEx
  }

  def getManyByIds(ids: UUID*): Task[Seq[T]] = getManyFilter(BSONDocument(
    "_id" -> BSONDocument("$in" -> ids)
  ))

  override def getAll: Task[Seq[T]] = getMany()


  override def create(model: T): Task[T] = collection.flatMap {
    _.insert.one(model).map(_.code).wrapEx.flatMap {
      case None => Task.now(model)
      case Some(errCode) => Task.raiseError(DbResultException)
    }
  }

  override def update(model: T): Task[Unit] = collection.flatMap(
    _.update(ordered = false)
      .one(BSONDocument("_id" -> model.id), model, upsert=false, multi = false)
      .map(_ => ())
      .wrapEx
  )


  override def delete(id: UUID): Task[Unit] = collection.flatMap {
    _.findAndRemove(
      selector = BSONDocument("_id" -> id)
    ).wrapEx.map(_ => ())
  }

  def updateById(id: UUID, updateBody: ElementProducer*): Task[Unit] = {

    val updateModifier = BSONDocument(
      "$set" -> BSONDocument(
        updateBody:_*
      )
    )

    collection.flatMap {
      _.findAndUpdate(
        selector = BSONDocument("_id" -> id),
        update = updateModifier,
      ).wrapEx.map {res => ()}
    }
  }

  def getMany(filter: ElementProducer*): Task[Seq[T]] = getManyFilter(BSONDocument(filter:_*))

  def getManyFilter(filter: BSONDocument): Task[Seq[T]] = collection.flatMap(
    _.find(filter)
      .cursor[T]()
      .collect[Seq]()
      .wrapEx
  )

  def getManyWithShape[D : Reader : Writer](filter: ElementProducer*): Task[Seq[D]] = {
    val filterDoc = BSONDocument(filter: _*)
    collection.flatMap {
      _.find(filterDoc)
        .cursor[D]()
        .collect[Seq]()
        .wrapEx
    }
  }

}

