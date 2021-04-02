package models.dtos

import play.api.data.Form
import play.api.data.Forms._

case class NoteCreateDTO(text: String)

object NoteCreateDTO {
  implicit val form: Form[NoteCreateDTO] = Form(
    mapping(
      "text" -> nonEmptyText,
    )(NoteCreateDTO.apply)(NoteCreateDTO.unapply)
  )
}


