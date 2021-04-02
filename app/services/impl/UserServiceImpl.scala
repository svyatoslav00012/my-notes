package services.impl

import controllers.utils.Exceptions.NotFoundException
import daos.UserDAO
import models.dtos.UserDTO
import monix.eval.Task
import services.UserService

import javax.inject.Inject

class UserServiceImpl @Inject()(userDAO: UserDAO) extends UserService {
  override def getUser(userId: Long): Task[UserDTO] = userDAO.getById(userId).map{
    case Some(user) => user.toDTO
    case _ => throw NotFoundException("user", s"id=$userId")
  }
}
