package daos.redis

import daos.UserDAO
import daos.redis.generic.RedisBaseDAOImpl
import models.User
import monix.eval.Task
import services.Cache

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UserDAORedisImpl @Inject()(cache: Cache)
                                (implicit ex: ExecutionContext)
  extends RedisBaseDAOImpl[User, Long]("user", cache) with UserDAO {
  override def getByEmail(email: String): Task[Option[User]] = for {
    all <- getAll
  } yield all.find(_.email == email)

  override def create(model: User): Task[User] = {
    var freeId = -1
    mutate { old =>
      val exists = old.exists(_.id == model.id)
      if (exists) {
        throw new Exception("Already Exists")
      }
      freeId = (0 to 10000).find(id => !old.exists(_.id == id)).get
      old :+ model.copy(id = freeId)
    }.map(_ => model.copy(id = freeId))
  }
}