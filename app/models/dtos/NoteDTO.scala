package models.dtos

import play.api.libs.json.{Json, OWrites}

import java.util.UUID

case class NoteDTO(_id: UUID, text: String, updatedAt: Long, createdAt: Long)

object NoteDTO{
  implicit val writes: OWrites[NoteDTO] = Json.writes[NoteDTO]
}






