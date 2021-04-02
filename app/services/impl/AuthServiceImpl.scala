package services.impl

import constants.TokenType
import constants.TokenType.CONFIRM_EMAIL
import controllers.utils.Exceptions.{NotFoundException, TokenBrokenOrExpired, UserAlreadyExist, WrongCredentials}
import daos.{TokenDAO, UserDAO}
import models.{Token, User}
import models.dtos.{Credentials, UserDTO}
import monix.eval.Task
import services.{AuthService, MailerService}
import services.helpers.{BCryptHelper, TimeHelper, UUIDGenerator}

import javax.inject.Inject

class AuthServiceImpl @Inject()(userDAO: UserDAO,
                                tokenDAO: TokenDAO,
                                mailerService: MailerService) extends AuthService {

  override def signIn(credentials: Credentials)(implicit bcryptH: BCryptHelper): Task[UserDTO] = for {
    byEmail <- userDAO.getByEmail(credentials.email)
    validPassword <- byEmail match {
      case Some(user) => bcryptH.check(credentials.password, user.password)
      case _ => Task.now(false)
    }
    userDTO = (byEmail, validPassword) match {
      case (Some(user), true) if user.isEmailConfirmed => user.toDTO
      case _ => throw WrongCredentials
    }
  } yield userDTO


  override def signUp(credentials: Credentials)(implicit bcryptH: BCryptHelper,
                                                uuidH: UUIDGenerator,
                                                timeH: TimeHelper): Task[Unit] = for {
    byEmail <- userDAO.getByEmail(credentials.email)
    checkExisting <- byEmail match {
      case Some(u) => Task.raiseError(UserAlreadyExist)
      case _ => Task.now(Unit)
    }
    hashedPassword <- bcryptH.bcrypt(credentials.password)
    newUser <- userDAO.create(createUser(credentials.email, hashedPassword))
    token = Token(-1, CONFIRM_EMAIL, newUser.id, uuidH.generate.toString, timeH.now)
    _ <- tokenDAO.create(token)
    _ <- mailerService.sendConfirmEmail(newUser.email, token.token)
  } yield ()


  def confirmEmail(token: String)(implicit timeHelper: TimeHelper): Task[Unit] = for {
    tokenModel <- tokenDAO.getByTokenStringAndType(token, CONFIRM_EMAIL).map{
      case Some(t) if !isExpired(t) => t
      case _ => throw TokenBrokenOrExpired
    }
    userByToken <- getUser(tokenModel.userId)
    _ <- tokenDAO.delete(tokenModel.id)
    _ <- userDAO.update(userByToken.copy(isEmailConfirmed = true))
  } yield ()

  private def getUser(userId: Long): Task[User] = userDAO.getById(userId).map{
    case Some(t) => t
    case _ => throw NotFoundException("user", s"id=$userId")
  }

  private def isExpired(token: Token)(implicit timeHelper: TimeHelper): Boolean =
    timeHelper.now.toEpochMilli - token.createdAt.toEpochMilli > Token.TTL

  private def createUser(email: String, password: String)(implicit timeHelper: TimeHelper): User = User(
    id = -1,
    email, password,
    isEmailConfirmed = false,
    createdAt = timeHelper.now,
    updatedAt = timeHelper.now
  )
}
