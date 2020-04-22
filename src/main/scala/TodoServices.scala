import api.TasksApi
import quill.TasksQuill
import utils.{DatabaseConfig, MainConfig, Md5Hasher, MigrationService, QuillAuthenticator, TaskToJsonMapping}

trait TodoServices {
  val hasher = new Md5Hasher()
  val databaseConfig = new DatabaseConfig {}
  val tasksQuill = new TasksQuill(databaseConfig, hasher)
  val authenticator = new QuillAuthenticator(tasksQuill, hasher)
  val mapping = new TaskToJsonMapping {}


  val taskApi = new TasksApi(authenticator, tasksQuill, mapping)
  val routeService = new Routes(taskApi)

  val config = MainConfig.config

  val migrationService = new MigrationService(config)
}
