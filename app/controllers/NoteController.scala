package controllers

import controllers.utils.ControllerUtils
import models.dtos.{NoteCreateDTO, NoteEditDTO}
import monix.execution.Scheduler
import play.api.libs.json.Json
import play.api.mvc._
import services.NoteService

import javax.inject._
import scala.concurrent.ExecutionContext


@Singleton
class NoteController @Inject()(cc: ControllerComponents,
                               noteService: NoteService)(implicit ex: ExecutionContext, sch: Scheduler)
  extends ControllerUtils(cc) {

  def getAllAll: Action[AnyContent] = authorizedAction { _ =>
    noteService.getAll().map { notes =>
      val arrayOfJS = notes.map(Json.toJson(_))
      Ok(Json.toJson(arrayOfJS))
    }
  }

  def getAll: Action[AnyContent] = authorizedAction { request =>
    noteService.getByUser(request.userId).map { notes =>
      val arrayOfJS = notes.map(Json.toJson(_))
      Ok(Json.toJson(arrayOfJS))
    }
  }

  def getById(noteId: String): Action[AnyContent] = authorizedAction { request =>
    withValidUUID(noteId) { uuid =>
      noteService.getById(uuid, request.userId).map {
        note => Ok(Json.toJson(note))
      }
    }
  }

  def create: Action[AnyContent] = authorizedActionWithBody[NoteCreateDTO] { implicit request =>
      noteService.create(request.parsedBody, request.userId).map(_ => Ok)
  }

  def update: Action[AnyContent] = authorizedActionWithBody[NoteEditDTO] { implicit request =>
     noteService.update(request.parsedBody, request.userId).map(_ => Ok)
  }

  def delete(noteId: String): Action[AnyContent] = authorizedAction { implicit request =>
    withValidUUID(noteId) { uuid =>
      noteService.delete(uuid, request.userId).map(_ => Ok)
    }
  }

}
