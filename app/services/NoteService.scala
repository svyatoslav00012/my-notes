package services

import models.Note
import models.dtos.{NoteCreateDTO, NoteDTO, NoteEditDTO}
import monix.eval.Task
import services.helpers.{TimeHelper, UUIDGenerator}

import java.util.UUID

trait NoteService {
  def getAll(): Task[Seq[Note]]
  def getByUser(currentUserId: Long): Task[Seq[NoteDTO]]
  def getById(noteId: UUID, currentUserId: Long): Task[NoteDTO]
  def create(model: NoteCreateDTO, currentUserId: Long)
            (implicit uuidG: UUIDGenerator, timeHelper: TimeHelper): Task[Unit]
  def update(dto: NoteEditDTO, currentUserId: Long)(implicit timeHelper: TimeHelper): Task[Unit]
  def delete(noteId: UUID, currentUserId: Long): Task[Unit]
}
