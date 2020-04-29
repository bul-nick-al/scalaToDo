package todo.quill

import com.mysql.cj.protocol.ResultListener
import todo.models.{Id, Task, User}
import todo.utils.{DatabaseConfig, Hasher}
import monix.execution.Scheduler.Implicits.global
import todo.services.TasksService

import scala.concurrent.Future

class TasksQuill(val dbConfig: DatabaseConfig, userQuill: UserQuill, hasher: Hasher) extends TasksService {

  import dbConfig.ctx
  import dbConfig.ctx._

  def findAllFor(user: User): Future[List[Task]] = ctx
    .run(quote {
      query[Task].filter(_.userId == lift(user.id))
    })
    .runToFuture

  def findAllFor(user: User, completed: Boolean): Future[List[Task]] = ctx
    .run(quote {
      query[Task].filter(task => task.userId == lift(user.id) && task.completed == lift(completed))
    })
    .runToFuture

  def findByIdFor(user: User, taskId: Id): Future[Option[Task]] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id))
    })
    .map(_.headOption)
    .runToFuture

  def create(task: Task): Future[Id] =
    userQuill.findUserById(task.userId).flatMap {
      case Some(_) =>
        ctx
          .run(quote {
            query[Task].insert(lift(task)).returningGenerated(_.id)
          })
          .runToFuture
      case None => ctx.run(0L).runToFuture
    }

  def update(newTask: Task): Future[Id] = ctx
    .run(
      quote {
        query[Task].filter(_.id == lift(newTask.id)).update(lift(newTask))
      }
    )
    .runToFuture

  def deleteFor(user: User, taskId: Id): Future[Id] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id)).delete
    })
    .runToFuture

}
