package todo

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.logRequestResult
import todo.services.TodoServices

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

object Main extends App with TodoServices {

  implicit val actors: ActorSystem                = ActorSystem()
  implicit val executionContext: ExecutionContext = actors.dispatcher
  protected val log: LoggingAdapter               = Logging(actors, getClass)

  migrationService.migrate()

  for {
    binding <- Http().bindAndHandle(
                handler = logRequestResult("log")(routeService.routes),
                interface = config.http.interface,
                port = config.http.port
              )
    _ = sys.addShutdownHook {
      for {
        _ <- binding.terminate(Duration(5, TimeUnit.SECONDS))
        _ <- actors.terminate()
      } yield ()
    }
  } yield ()
}
