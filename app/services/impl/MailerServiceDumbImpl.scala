package services.impl

import monix.eval.Task
import services.MailerService

class MailerServiceDumbImpl extends MailerService {
  override def sendConfirmEmail(email: String, token: String): Task[Unit] = Task.now{
    println(s"Confirm your Email! token: $token")
  }
}
