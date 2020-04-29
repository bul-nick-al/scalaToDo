package todo.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, concat, get, parameter, path}
import todo.utils.{Authenticator, TaskToJsonMapping}
import todo.quill.TasksQuill
import spray.json._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import todo.models.{Id, Task, User}
import monix.execution.Scheduler.Implicits.global
import todo.services.RepositoryService

import scala.util.{Failure, Success}

class TasksApi(authenticator: Authenticator, qs: RepositoryService, taskToJsonMapping: TaskToJsonMapping) {

  import taskToJsonMapping._

  private val securedRoute = Route.seal {
    (path("task") & parameter("id".as[Int].?)) { id =>
      authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
        get {
          id match {
            case Some(value) =>
              onComplete(qs.tasks.findByIdFor(user, value)) {
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
      (path("tasks") & parameter("completed".as[Boolean].?) & get) { completed =>
        authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
          completed match {
            case Some(completed) => complete(qs.tasks.findAllFor(user, completed).map(_.toJson))
            case None            => complete(qs.tasks.findAllFor(user).map(_.toJson))
          }
        }
      } ~
      path("tasks") {
        post {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            entity(as[Task]) { task =>
              complete(qs.tasks.create(task.withUser(user)).map(_.toJson))
            }
          }
        }
      } ~
      path("tasks") {
        put {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            entity(as[Task]) { task =>
              complete(qs.tasks.update(task.withUser(user)).map(_.toJson))
            }
          }
        }
      } ~
      (path("task") & parameter("id".as[Int].?)) { id =>
        delete {
          authenticateBasicAsync(realm = "secure site", authenticator.authenticate) { user =>
            id match {
              case Some(id) => complete(qs.tasks.deleteFor(user, id).map(_.toJson))
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
          complete(qs.user.create(user).map(_.toJson))
        }
      }
    }

  val tasksApi: Route = publicRoute ~ securedRoute
}
