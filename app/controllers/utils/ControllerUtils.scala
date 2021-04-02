package controllers.utils

import controllers.utils.Exceptions.{ForbiddenException, NotFoundException, StrIsNotUUIDException, TokenBrokenOrExpired, UnauthorizedException, UserAlreadyExist, WrongCredentials, WrongJsonException}
import monix.eval.Task
import monix.execution.Scheduler
import play.api.data.Form
import play.api.mvc._

import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

class ControllerUtils(cc: ControllerComponents)(implicit ex: ExecutionContext, sch: Scheduler) extends AbstractController(cc){

  def withValidUUID(strUUID: String)(block: UUID => Task[Result]): Task[Result] = {
    Try(UUID.fromString(strUUID)) match {
      case Success(uuid) => block(uuid)
      case Failure(err) => Task.raiseError(StrIsNotUUIDException)
    }
  }

  def actionWithRecover(block: CustomRequest[Any] => Task[Result]): Action[AnyContent] =
    Action.async {
      req =>
        block(CustomRequest(req, None, None)).onErrorRecover {
          //401
          case UnauthorizedException => Unauthorized
          //403
          case ForbiddenException(msg) => Forbidden(msg)
          //404
          case NotFoundException(msg) => NotFound(msg)
          //409
          case UserAlreadyExist => Conflict
          //417
          case WrongCredentials => ExpectationFailed
          case TokenBrokenOrExpired => ExpectationFailed
          //422
          case WrongJsonException(msg) => UnprocessableEntity(msg.mkString)
          case StrIsNotUUIDException => UnprocessableEntity

          //500
          case e =>
            e.printStackTrace()
            InternalServerError
        }.runToFuture
    }

  def actionWithBody[A](block: CustomRequest[A] => Task[Result])(implicit form: Form[A]): Action[AnyContent] =
    actionWithRecover(req => Task(req.bodyAsJson).flatMap(block))

  def authorizedAction(block: CustomRequest[Any] => Task[Result]): Action[AnyContent] =
    actionWithRecover(req => Task(req.withUserId).flatMap(block))

  def authorizedActionWithBody[A](block: CustomRequest[A] => Task[Result])(implicit form: Form[A]): Action[AnyContent] =
    authorizedAction(req => Task(req.bodyAsJson[A]).flatMap(block))

}
