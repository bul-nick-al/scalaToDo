package utils

import akka.http.scaladsl.server.directives.Credentials
import models.User
import quill.TasksQuill

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

trait Authenticator extends Hasher {
  def authenticate(credentials: Credentials): Future[Option[User]] =
    credentials match {
      case p @ Credentials.Provided(login) =>
        Future {
          TasksQuill.findUserByLogin(login).flatMap(user => if (p.verify(user.password, md5)) Some(user) else None)
        }
      case _ => Future.successful(None)
    }
}
