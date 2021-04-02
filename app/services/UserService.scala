package services

import models.dtos.UserDTO
import monix.eval.Task

trait UserService {
  def getUser(userId: Long): Task[UserDTO]
}
