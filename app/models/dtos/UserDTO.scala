package models.dtos

import play.api.libs.json.{Json, OWrites}

import java.time.Instant

case class UserDTO(id: Long,
                   email: String,
                   createdAt: Instant,
                   updatedAt: Instant)

object UserDTO {
  implicit val writes: OWrites[UserDTO] = Json.writes[UserDTO]
}
