package todo

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import todo.api.TasksApi

class Routes(api: TasksApi) {
  val routes: Route = pathPrefix("todo/api") {
    api.tasksApi
  }
}
