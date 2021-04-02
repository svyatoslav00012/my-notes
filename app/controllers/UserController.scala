package controllers

import controllers.utils.ControllerUtils
import monix.execution.Scheduler
import play.api.libs.json.Json
import play.api.mvc._
import services.UserService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               userService: UserService)(implicit ex: ExecutionContext, sch: Scheduler)
  extends ControllerUtils(cc) {

  def getCurrentUser: Action[AnyContent] = authorizedAction { req =>
    userService.getUser(req.userId)
      .map(dto => Ok(Json.toJson(dto)))
  }

}
