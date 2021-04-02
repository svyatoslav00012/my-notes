package services

import models.dtos.{Credentials, UserDTO}
import monix.eval.Task
import services.helpers.{BCryptHelper, TimeHelper, UUIDGenerator}

trait AuthService {
  def signIn(credentials: Credentials)(implicit bcryptH: BCryptHelper): Task[UserDTO]

  def signUp(credentials: Credentials)(implicit bcryptH: BCryptHelper,
                                       uuidGenerator: UUIDGenerator,
                                       timeHelper: TimeHelper): Task[Unit]

  def confirmEmail(token: String)(implicit timeHelper: TimeHelper): Task[Unit]
}
