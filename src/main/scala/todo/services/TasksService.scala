package todo.services

import todo.models.{Id, Task, User}

import scala.concurrent.Future

trait TasksService {

  def findAll: Future[List[Task]]

  def findAllFor(user: User, completed: Boolean): Future[List[Task]]

  def findAllFor(user: User): Future[List[Task]]

  def findById(taskId: Id): Future[Option[Task]]

  def findByIdFor(user: User, taskId: Id): Future[Option[Task]]

  def create(task: Task): Future[Id]

  def update(newTask: Task): Future[Id]

  def updateFor(user: User, newTask: Task): Future[Id]

  def delete(taskId: Id): Future[Id]

  def deleteFor(user: User, taskId: Id): Future[Id]
}