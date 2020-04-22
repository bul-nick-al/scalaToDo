package todo

import todo.api.TasksApi
import todo.quill.{QuillService, TasksQuill, UserQuill}
import todo.utils._

trait TodoServices {
  val hasher                         = new Md5Hasher()
  val databaseConfig: DatabaseConfig = new DatabaseConfig {}

  val quillService: QuillService = new QuillService {
    override val tasksQuill: TasksQuill = new TasksQuill(databaseConfig, userQuill, hasher)
    override lazy val userQuill: UserQuill =  new UserQuill(databaseConfig, hasher)
  }

  val authenticator                  = new QuillAuthenticator(quillService.userQuill, hasher)
  val mapping: TaskToJsonMapping     = new TaskToJsonMapping {}

  val taskApi      = new TasksApi(authenticator, quillService, mapping)
  val routeService = new Routes(taskApi)

  val config: Config = MainConfig.config

  val migrationService = new MigrationService(config)
}