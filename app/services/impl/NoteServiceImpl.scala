package services.impl

import com.google.inject.Inject
import controllers.utils.Exceptions.{ForbiddenException, NotFoundException}
import daos.NoteDAO
import models.Note
import models.dtos.{NoteCreateDTO, NoteDTO, NoteEditDTO}
import monix.eval.Task
import services.NoteService
import services.helpers.{TimeHelper, UUIDGenerator}

import java.time.Instant
import java.util.UUID
import java.util.concurrent.Future
import javax.inject.Singleton

@Singleton
class NoteServiceImpl @Inject()(noteDAO: NoteDAO) extends NoteService {
  override def getAll(): Task[Seq[Note]] = noteDAO.getAll

  override def getByUser(currentUserId: Long): Task[Seq[NoteDTO]] = noteDAO.getByUserId(currentUserId).map {
    notes => notes.map(_.toNoteDTO)
  }

  override def getById(noteId: UUID, currentUserId: Long): Task[NoteDTO] =
    ifExistsAndOwner(noteId, currentUserId)(note => Task.now(note.toNoteDTO))

  override def create(model: NoteCreateDTO, authorId: Long)
                     (implicit uuidG: UUIDGenerator, timeHelper: TimeHelper): Task[Unit] =
    noteDAO.create(Note(
      _id = uuidG.generate,
      text = model.text,
      authorId = authorId,
      createdAt = timeHelper.now,
      updatedAt = timeHelper.now
    )).map(_ => Unit)

  override def update(dto: NoteEditDTO, currentUserId: Long)(implicit timeHelper: TimeHelper): Task[Unit] = ifExistsAndOwner(dto._id, currentUserId) {
    oldNote => noteDAO.update(oldNote.copy(
      text = dto.text,
      updatedAt = timeHelper.now
    ))
  }

  override def delete(noteId: UUID, currentUserId: Long): Task[Unit] = ifExistsAndOwner(noteId, currentUserId) {
    note => noteDAO.delete(note._id)
  }

  private def ifExists[T](noteId: UUID)(block: Note => Task[T]): Task[T] = noteDAO.getById(noteId) flatMap {
    case Some(n) => block(n)
    case _ => Task.raiseError[T](NotFoundException("note", s"id=$noteId"))
  }

  private def ifExistsAndOwner[T](noteId: UUID, userId: Long)(block: Note => Task[T]): Task[T] = ifExists(noteId) {
    case n if n.authorId != userId => Task.raiseError(ForbiddenException("User is not owner of note"))
    case n => block(n)
  }


}
