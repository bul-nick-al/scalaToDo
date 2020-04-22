package todo.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, concat, get, parameter, path}
import todo.utils.{Authenticator, TaskToJsonMapping}
import todo.quill.{QuillService, TasksQuill}
import spray.json._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import todo.models.{Id, Task, User}
import monix.execution.Scheduler.Implicits.global

import scala.util.{Failure, Success}

class TasksApi(authenticator: Authenticator, qs: QuillService, taskToJsonMapping: TaskToJsonMapping) {

  import taskToJsonMapping._
  private val securedRoute = Route.seal {
    (path("task") & parameter("id".as[Int].?)) { id =>
      authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
        get {
          id match {
            case Some(value) =>
              onComplete(qs.tasksQuill.findByIdFor(user, value).runToFuture) {
                case Success(taskOptional) =>
                  taskOptional match {
                    case Some(task) => complete(task.toJson)
                    case None       => complete(StatusCodes.NotFound -> "There is no task with such id")
                  }
                case Failure(ex) => complete(StatusCodes.InternalServerError -> s"An error occurred: ${ex.getMessage}")
              }
            case None => complete(StatusCodes.BadRequest -> "Id must be a number")
          }
        }
      }
    } ~
      (path("tasks") & get) {
        authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
          complete(qs.tasksQuill.findAllFor(user).map(_.toJson).runToFuture)
        }
      } ~
      path("tasks") {
        post {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            entity(as[Task]) { task =>
              complete(qs.tasksQuill.create(task.withUser(user)).map(_.toJson).runToFuture)
            }
          }
        }
      } ~
      path("tasks") {
        put {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            entity(as[Task]) { task =>
              complete(qs.tasksQuill.update(task.withUser(user)).map(_.toJson).runToFuture)
            }
          }
        }
      } ~
      (path("task") & parameter("id".as[Int].?)) { id =>
        delete {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            id match {
              case Some(id) => complete(qs.tasksQuill.deleteFor(user, id).map(_.toJson).runToFuture)
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
          complete(qs.userQuill.create(user).map(_.toJson).runToFuture)
        }
      }
    }

  val tasksApi: Route = publicRoute ~ securedRoute
}
