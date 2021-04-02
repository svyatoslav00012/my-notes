package models

import models.dtos.UserDTO
import play.api.libs.json.{Json, OFormat}

import java.time.Instant

case class User(id: Long,
                email: String,
                password: String,
                isEmailConfirmed: Boolean,
                createdAt: Instant,
                updatedAt: Instant) extends HasId[Long] {
  def toDTO: UserDTO = UserDTO(id, email, createdAt, updatedAt)
}

object User {
  implicit val format: OFormat[User] = Json.format[User]

  def tupled: ((Long, String, String, Boolean, Instant, Instant)) => User = (User.apply _).tupled
}




