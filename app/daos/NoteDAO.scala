package daos

import models.Note
import monix.eval.Task

import java.util.UUID

trait NoteDAO extends ModelDAO[Note, UUID]{
  def getByUserId(userId: Long): Task[Seq[Note]]
}
