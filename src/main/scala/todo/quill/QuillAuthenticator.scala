package todo.quill

import akka.http.scaladsl.server.directives.Credentials
import monix.execution.Scheduler
import todo.models.User
import todo.services.UserService
import todo.utils.{Authenticator, Hasher}

import scala.concurrent.Future

class QuillAuthenticator(userQuill: UserService, hasher: Hasher) extends Authenticator {
  def authenticate(credentials: Credentials)(implicit sc: Scheduler): Future[Option[User]] =
    credentials match {
      case p @ Credentials.Provided(login) =>
        userQuill
          .findUserByLogin(login)
          .map(_.flatMap(user => if (p.verify(user.password, hasher.hash)) Some(user) else None))
      case _ => Future.successful(None)
    }
}