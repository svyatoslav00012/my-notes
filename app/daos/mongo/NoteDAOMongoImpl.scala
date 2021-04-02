package daos.mongo

import daos.NoteDAO
import daos.mongo.generic.MongoBaseDAOImpl
import models.Note
import monix.eval.Task
import monix.execution.Scheduler
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.{Index, IndexType}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class NoteDAOMongoImpl @Inject()(reactiveMongoApi: ReactiveMongoApi)
                                (implicit ex: ExecutionContext)
  extends MongoBaseDAOImpl[Note](reactiveMongoApi, "note") with NoteDAO{

  collection.map(c => c.indexesManager.create(Index(key = Seq(("authorId", IndexType.Ascending)), name = Some("authorId index")))).runToFuture(Scheduler.global)

  override def getByUserId(userId: Long): Task[Seq[Note]] = getMany("authorId" -> userId)
}
