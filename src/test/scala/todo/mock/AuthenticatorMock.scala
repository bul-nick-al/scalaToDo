package todo.mock

import akka.http.scaladsl.server.directives.Credentials
import monix.execution.Scheduler
import todo.models.User
import todo.utils.Authenticator

import scala.concurrent.Future

class AuthenticatorMock extends Authenticator{
  override def authenticate(credentials: Credentials)(implicit sc: Scheduler): Future[Option[User]] = {
    val result = credentials match {
      case p @ Credentials.Provided(login) =>
        if (login == "login" && p.verify("password")) Some(user1) else None
      case _ => None
    }
    Future.successful(result)
  }
}
