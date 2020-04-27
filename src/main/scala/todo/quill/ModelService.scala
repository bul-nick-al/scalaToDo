package todo.quill

trait ModelService {
  val tasks: TasksService
  val user: UserService
}
