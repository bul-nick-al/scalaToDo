package todo.services

import todo.models.{Id, User}

import scala.concurrent.Future

trait UserService {
  def findByCredentials(login: String, password: String): Future[Option[User]]

  def findUserById(userId: Id): Future[Option[User]]

  def findUserByLogin(login: String): Future[Option[User]]

  def create(user: User): Future[Id]
}
