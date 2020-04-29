package todo.services

import todo.Routes
import todo.api.TasksApi
import todo.quill.{QuillAuthenticator, TasksQuill, UserQuill}
import todo.utils._

trait TodoServices {
  val hasher                         = new Md5Hasher()
  val databaseConfig: DatabaseConfig = new DatabaseConfig {}

  val quillService: ModelService = new ModelService {
    override val tasks: TasksQuill = new TasksQuill(databaseConfig, user, hasher)
    override lazy val user: UserQuill =  new UserQuill(databaseConfig, hasher)
  }

  val authenticator                  = new QuillAuthenticator(quillService.user, hasher)
  val mapping: TaskToJsonMapping     = new TaskToJsonMapping {}

  val taskApi      = new TasksApi(authenticator, quillService, mapping)
  val routeService = new Routes(taskApi)

  val config: Config = MainConfig.config

  val migrationService = new MigrationService(config)
}
