import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.TasksApi

class Routes(api: TasksApi) {
  val routes: Route = pathPrefix("api") {
    api.tasksApi
  }
}
