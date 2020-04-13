import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import models.{Task, User}
import quill.TasksQuill
import utils.{MainConfig, MigrationService}

import scala.concurrent.ExecutionContext

object Main extends App with MainConfig with MigrationService with Routes {

  implicit val actors: ActorSystem                = ActorSystem()
  implicit val executionContext: ExecutionContext = actors.dispatcher
  protected val log: LoggingAdapter               = Logging(actors, getClass)

  Http().bindAndHandle(
    handler = logRequestResult("log")(routes),
    interface = config.http.interface,
    port = config.http.port
  )
}
