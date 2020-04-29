package todo.quill

import todo.models.{Id, User}
import todo.utils.{DatabaseConfig, Hasher}
import monix.execution.Scheduler.Implicits.global
import todo.services.UserService

import scala.concurrent.Future

class UserQuill(val dbConfig: DatabaseConfig, hasher: Hasher) extends UserService {

  import dbConfig.ctx
  import dbConfig.ctx._

  def findByCredentials(login: String, password: String): Future[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login) && user.password == lift(hasher.hash(password)))
    })
    .map(_.headOption)
    .runToFuture

  def findUserById(userId: Id): Future[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.id == lift(userId))
    })
    .map(_.headOption)
    .runToFuture

  def findUserByLogin(login: String): Future[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login))
    })
    .map(_.headOption)
    .runToFuture

  def create(user: User): Future[Id] =
    ctx
      .run(quote {
        query[User]
          .insert(_.login -> lift(user.login), _.password -> lift(hasher.hash(user.password)))
          .returningGenerated(_.id)
      })
      .runToFuture
}
