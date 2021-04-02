package controllers

import controllers.utils.{ControllerUtils, CustomRequest}
import models.dtos.Credentials
import monix.eval.Task
import monix.execution.Scheduler
import play.api.libs.json.Json
import play.api.mvc._
import services.AuthService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               authService: AuthService)(implicit ex: ExecutionContext,
                                                         sch: Scheduler)
  extends ControllerUtils(cc) {

  def signIn: Action[AnyContent] = actionWithBody[Credentials]{
    req => authService.signIn(req.parsedBody)
      .map(dto => Ok(Json.toJson(dto))
        .withSession(CustomRequest.UserIdKey -> dto.id.toString)
      )
  }(Credentials.signInForm)


  def signUp: Action[AnyContent] = actionWithBody[Credentials] {
    req => authService.signUp(req.parsedBody)
      .map(_ => Ok)
  }(Credentials.signUpForm)


  def confirmEmail(token: String): Action[AnyContent] = actionWithRecover {
    req => authService.confirmEmail(token).map(_ => Ok)
  }


  def signOut: Action[AnyContent] = actionWithRecover{
    _ => Task.now(Ok.withNewSession)
  }

}