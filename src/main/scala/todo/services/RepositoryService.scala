package todo.services

trait RepositoryService {
  val tasks: TasksService
  val user: UserService
}
