package services

import monix.eval.Task

trait MailerService {
  def sendConfirmEmail(email: String, token: String): Task[Unit]
}
