package todo.utils

import akka.http.scaladsl.server.directives.Credentials
import todo.models.User
import monix.execution.Scheduler

import scala.concurrent.Future

trait Authenticator {
  def authenticate(credentials: Credentials)(implicit sc: Scheduler): Future[Option[User]]
}
