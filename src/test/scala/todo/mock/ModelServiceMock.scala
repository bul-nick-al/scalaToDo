package todo.mock

import monix.eval.Task
import todo.models.{Id, Task, User}
import todo.quill.{ModelService, TasksQuill, TasksService, UserQuill}

//class ModelServiceMock extends ModelService {
//  override val tasks: TasksQuill = new TasksService() {
//    override def findAll: Task[List[Task]] = ???
//
//    override def findAllFor(user: User): Task[List[Task]] = ???
//
//    override def findById(taskId: Id): Task[Option[Task]] = ???
//
//    override def findByIdFor(user: User, taskId: Id): Task[Option[Task]] = ???
//
//    override def create(task: Task): Task[Id] = ???
//
//    override def update(newTask: Task): Task[Id] = ???
//
//    override def updateFor(user: User, newTask: Task): Task[Id] = ???
//
//    override def delete(taskId: Id): Task[Id] = ???
//
//    override def deleteFor(user: User, taskId: Id): Task[Id] = ???
//  }
//  override val user: UserQuill = _
//}
//
//object D