package models

import play.api.libs.json.{Json, OFormat}

import java.time.Instant

case class Token(id: Long,
                 tokenType: String,
                 userId: Long,
                 token: String,
                 createdAt: Instant) extends HasId[Long]

object Token{
  val TTL: Long = 5 * 60 * 1000

  implicit val format: OFormat[Token] = Json.format[Token]

  def tupled: ((Long, String, Long, String, Instant)) => Token = (this.apply _).tupled
}
