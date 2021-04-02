package models.dtos

import play.api.data.Form
import play.api.data.Forms._

import java.util.UUID

case class NoteEditDTO(_id: UUID, text: String)

object NoteEditDTO {
  implicit val form: Form[NoteEditDTO] = Form(
    mapping(
      "_id" -> uuid,
      "text" -> nonEmptyText,
    )(NoteEditDTO.apply)(NoteEditDTO.unapply)
  )
}




