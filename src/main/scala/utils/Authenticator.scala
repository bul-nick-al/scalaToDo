package utils

import akka.http.scaladsl.server.directives.Credentials
import models.User
import monix.execution.Scheduler
import quill.TasksQuill
import scala.concurrent.Future

trait Authenticator {
  def authenticate(credentials: Credentials)(implicit sc: Scheduler): Future[Option[User]]
}



class QuillAuthenticator(tasksQuill: TasksQuill, hasher: Hasher) extends Authenticator() {
  def authenticate(credentials: Credentials)(implicit sc: Scheduler): Future[Option[User]] =
    credentials match {
      case p @ Credentials.Provided(login) =>
        tasksQuill.findUserByLogin(login).runToFuture.map(_.flatMap(user => if (p.verify(user.password, hasher.hash)) Some(user) else None))
      case _ => Future.successful(None)
    }
}
