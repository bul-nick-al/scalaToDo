package api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, concat, get, parameter, path}
import utils.{Authenticator, TaskToJsonMapping}
import quill.TasksQuill
import spray.json._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import models.{Id, Task, User}

trait TasksApi extends TaskToJsonMapping with Authenticator {

  private val securedRoute = Route.seal {
    (path("task") & parameter("id".as[Int].?)) { id =>
      authenticateBasicAsync(realm = "secure site", authenticate) { user =>
        get {
          id match {
            case Some(value) =>
              TasksQuill.findByIdFor(user, value) match {
                case Some(task) => complete(task.toJson)
                case None       => complete(StatusCodes.NotFound -> "There is no task with such id")
              }
            case None => complete(StatusCodes.BadRequest -> "Id must be a number")
          }
        }
      }
    } ~
      (path("tasks") & get) {
        authenticateBasicAsync(realm = "secure site", authenticate) { user =>
          complete(TasksQuill.findAllFor(user).map(_.toJson))
        }
      } ~
      path("tasks") {
        post {
          authenticateBasicAsync(realm = "secure site", authenticate) { user =>
            entity(as[Task]) { task =>
              complete(TasksQuill.create(task.withUser(user)).toJson)
            }
          }
        }
      } ~
      path("tasks") {
        put {
          authenticateBasicAsync(realm = "secure site", authenticate) { user =>
            entity(as[Task]) { task =>
              complete(TasksQuill.update(task.withUser(user)).toJson)
            }
          }
        }
      } ~
      (path("task") & parameter("id".as[Int].?)) { id =>
        delete {
          authenticateBasicAsync(realm = "secure site", authenticate) { user =>
            id match {
              case Some(id) => complete(TasksQuill.deleteFor(user, id).toJson)
              case None     => complete(StatusCodes.BadRequest -> "Id must be a number")
            }
          }
        }
      }
  }

  private val publicRoute: Route =
    path("register") {
      post {
        entity(as[User]) { user =>
          complete(TasksQuill.create(user).toJson)
        }
      }
    }

  val tasksApi: Route = publicRoute ~ securedRoute
}
