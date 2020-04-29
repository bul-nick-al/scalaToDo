package todo.mock

import todo.models.{Id, Task, User}
import todo.services.{ModelService, TasksService, UserService}

import scala.concurrent.Future

class ModelServiceMockSuccessful extends ModelService {
  override val tasks: TasksService = new TasksService {
    override def findAll: Future[List[Task]] = Future.successful(listOfTasks)

    override def findAllFor(user: User): Future[List[Task]] = Future.successful(listOfTasks)

    override def findById(taskId: Id): Future[Option[Task]] = Future.successful(Some(task1))

    override def findByIdFor(user: User, taskId: Id): Future[Option[Task]] = Future.successful(Some(task2))

    override def create(task: Task): Future[Id] = Future.successful(1)

    override def update(newTask: Task): Future[Id] = Future.successful(2)

    override def updateFor(user: User, newTask: Task): Future[Id] = Future.successful(3)

    override def delete(taskId: Id): Future[Id] = Future.successful(4)

    override def deleteFor(user: User, taskId: Id): Future[Id] = Future.successful(5)

    override def findAllFor(user: User, completed: Boolean): Future[List[Task]] =
      if (completed)
        Future.successful(List(task2, task3))
      else
        Future.successful(List(task1, task4, task5))
  }
  override val user: UserService = new UserService {
    override def findByCredentials(login: String, password: String): Future[Option[User]] = Future.successful(Some(user1))

    override def findUserById(userId: Id): Future[Option[User]] = Future.successful(Some(user2))

    override def findUserByLogin(login: String): Future[Option[User]] = Future.successful(Some(user3))

    override def create(user: User): Future[Id] = Future.successful(1)
  }
}

class ModelServiceMockUnsuccessful extends ModelService {
  override val tasks: TasksService = new TasksService {
    override def findAll: Future[List[Task]] = Future.successful(List())

    override def findAllFor(user: User, completed: Boolean): Future[List[Task]] = Future.successful(List())

    override def findAllFor(user: User): Future[List[Task]] = Future.successful(List())

    override def findById(taskId: Id): Future[Option[Task]] = Future.successful(None)

    override def findByIdFor(user: User, taskId: Id): Future[Option[Task]] = Future.successful(None)

    override def create(task: Task): Future[Id] = Future.successful(0)

    override def update(newTask: Task): Future[Id] = Future.successful(0)

    override def updateFor(user: User, newTask: Task): Future[Id] = Future.successful(0)

    override def delete(taskId: Id): Future[Id] = Future.successful(0)

    override def deleteFor(user: User, taskId: Id): Future[Id] = Future.successful(0)
  }
  override val user: UserService = new UserService {
    override def findByCredentials(login: String, password: String): Future[Option[User]] = Future.successful(None)

    override def findUserById(userId: Id): Future[Option[User]] = Future.successful(None)

    override def findUserByLogin(login: String): Future[Option[User]] = Future.successful(None)

    override def create(user: User): Future[Id] = Future.successful(0)
  }
}
