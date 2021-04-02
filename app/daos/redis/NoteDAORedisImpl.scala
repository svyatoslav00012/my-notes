package daos.redis

import daos.NoteDAO
import daos.redis.generic.RedisBaseDAOImpl
import models.Note
import monix.eval.Task
import services.Cache

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class NoteDAORedisImpl @Inject()(cache: Cache)
                                (implicit ex: ExecutionContext)
  extends RedisBaseDAOImpl[Note, UUID]("note", cache) with NoteDAO {

  override def getByUserId(userId: Long): Task[List[Note]] =
    getAll.map(all => all.filter(_.authorId == userId))

}