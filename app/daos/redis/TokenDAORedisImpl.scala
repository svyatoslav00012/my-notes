package daos.redis

import daos.TokenDAO
import daos.redis.generic.RedisBaseDAOImpl
import models.Token
import monix.eval.Task
import services.Cache

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TokenDAORedisImpl @Inject()(cache: Cache)
                                 (implicit ex: ExecutionContext)
  extends RedisBaseDAOImpl[Token, Long]("token", cache) with TokenDAO {

  def getByTokenStringAndType(token: String, tokenType: String): Task[Option[Token]] = for {
    all <- getAll
  } yield all.find(t => t.token == token && t.tokenType == tokenType)


  def getByUserId(userId: Long): Task[List[Token]] = for {
    all <- getAll
  } yield all.filter(_.userId == userId)

  override def create(model: Token): Task[Token] = {
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