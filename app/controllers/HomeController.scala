package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(<a href="/docs/swagger-ui/index.html?url=/assets/swagger.json#/">swagger</a>).as("text/html")
  }

  def hello(name: String): Action[AnyContent] = Action {
    Ok(s"Hello, $name")
  }

}
