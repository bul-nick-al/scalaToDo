package todo.services

trait ModelService {
  val tasks: TasksService
  val user: UserService
}
