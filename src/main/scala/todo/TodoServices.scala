package todo

import todo.api.TasksApi
import todo.quill.TasksQuill
import todo.utils._

trait TodoServices {
  val hasher = new Md5Hasher()
  val databaseConfig: DatabaseConfig = new DatabaseConfig {}
  val tasksQuill = new TasksQuill(databaseConfig, hasher)
  val authenticator = new QuillAuthenticator(tasksQuill, hasher)
  val mapping: TaskToJsonMapping = new TaskToJsonMapping {}


  val taskApi = new TasksApi(authenticator, tasksQuill, mapping)
  val routeService = new Routes(taskApi)

  val config: Config = MainConfig.config

  val migrationService = new MigrationService(config)
}
