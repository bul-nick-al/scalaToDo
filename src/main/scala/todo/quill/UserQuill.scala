package todo.quill

import todo.models.{Id, User}
import todo.utils.{DatabaseConfig, Hasher}

abstract class UserService(val dbConfig: DatabaseConfig) {
  import dbConfig.ctx._

  def findByCredentials(login: String, password: String): Result[Option[User]]

  def findUserById(userId: Id): Result[Option[User]]

  def findUserByLogin(login: String): Result[Option[User]]

  def create(user: User): Result[Id]
}

class UserQuill(override val dbConfig: DatabaseConfig, hasher: Hasher) extends UserService(dbConfig) {

  import dbConfig.ctx
  import dbConfig.ctx._

  def findByCredentials(login: String, password: String): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login) && user.password == lift(hasher.hash(password)))
    })
    .map(_.headOption)

  def findUserById(userId: Id): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.id == lift(userId))
    })
    .map(_.headOption)

  def findUserByLogin(login: String): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login))
    })
    .map(_.headOption)

  def create(user: User): Result[Id] =
    ctx.run(quote {
      query[User]
        .insert(_.login -> lift(user.login), _.password -> lift(hasher.hash(user.password)))
        .returningGenerated(_.id)
    })
}
