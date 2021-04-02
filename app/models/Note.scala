package models

import models.dtos.NoteDTO
import play.api.libs.json.{Json, OFormat}
import reactivemongo.api.bson.{BSONDocumentReader, BSONDocumentWriter, Macros}

import java.time.Instant
import java.util.UUID

case class Note(_id: UUID,
                text: String,
                authorId: Long,
                createdAt: Instant,
                updatedAt: Instant) extends HasId[UUID] {

  def toNoteDTO: NoteDTO = NoteDTO(_id, text, updatedAt.toEpochMilli, createdAt.toEpochMilli)

  override val id: UUID = _id
}

object Note {
  implicit val format: OFormat[Note] = Json.format[Note]

  implicit val writer: BSONDocumentWriter[Note] = Macros.writer[Note]
  implicit val reader: BSONDocumentReader[Note] = Macros.reader[Note]
}
