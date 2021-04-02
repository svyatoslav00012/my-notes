package models.dtos

import play.api.data.Form
import play.api.data.Forms._

case class Credentials(email: String, password: String)

object Credentials {

  def validPassword(password: String): Boolean = {
    password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$")
  }

  implicit val signUpForm: Form[Credentials] = Form (
    mapping(
      "email" -> email,
      "password" -> nonEmptyText.verifying(validPassword _)
    )(Credentials.apply)(Credentials.unapply)
  )

  implicit val signInForm: Form[Credentials] = Form (
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Credentials.apply)(Credentials.unapply)
  )
}
