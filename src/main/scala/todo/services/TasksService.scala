package todo.services

import todo.models.{Id, Task, User}

import scala.concurrent.Future

trait TasksService {

  def findAllFor(user: User, completed: Boolean): Future[List[Task]]

  def findAllFor(user: User): Future[List[Task]]

  def findByIdFor(user: User, taskId: Id): Future[Option[Task]]

  def create(task: Task): Future[Id]

  def update(newTask: Task): Future[Id]

  def deleteFor(user: User, taskId: Id): Future[Id]
}
