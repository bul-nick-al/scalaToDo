package todo.quill

import com.mysql.cj.protocol.ResultListener
import todo.models.{Id, Task, User}
import todo.utils.{DatabaseConfig, Hasher}

abstract class TasksService(val dbConfig: DatabaseConfig) {
  import dbConfig.ctx._

  def findAll: Result[List[Task]]


  def findAllFor(user: User): Result[List[Task]]

  def findById(taskId: Id): Result[Option[Task]]

  def findByIdFor(user: User, taskId: Id): Result[Option[Task]]

  def create(task: Task): Result[Id]

  def update(newTask: Task): Result[Id]

  def updateFor(user: User, newTask: Task): Result[Id]

  def delete(taskId: Id): Result[Id]

  def deleteFor(user: User, taskId: Id): Result[Id]
}

class TasksQuill(override val dbConfig: DatabaseConfig, userQuill: UserQuill, hasher: Hasher) extends TasksService(dbConfig){

  import dbConfig.ctx
  import dbConfig.ctx._

  def findAll: Result[List[Task]] = ctx.run(quote {
    query[Task]
  })

  def findAllFor(user: User): Result[List[Task]] = ctx.run(quote {
    query[Task].filter(_.userId == lift(user.id))
  })

  def findById(taskId: Id): Result[Option[Task]] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId))
    })
    .map(_.headOption)

  def findByIdFor(user: User, taskId: Id): Result[Option[Task]] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id))
    })
    .map(_.headOption)

  def create(task: Task): Result[Id] =
    userQuill.findUserById(task.userId).flatMap {
      case Some(_) =>
        ctx.run(quote {
          query[Task].insert(lift(task)).returningGenerated(_.id)
        })
      case None => ctx.run(0L)
    }

  def update(newTask: Task): Result[Id] = ctx.run(
    quote {
      query[Task].filter(_.id == lift(newTask.id)).update(lift(newTask))
    }
  )

  def updateFor(user: User, newTask: Task): Result[Id] = ctx.run(quote {
    query[Task].filter(task => task.id == lift(newTask.id) && task.userId == lift(user.id)).update(lift(newTask))
  })

  def delete(taskId: Id): Result[Id] = ctx.run(quote {
    query[Task].filter(_.id == lift(taskId)).delete
  })

  def deleteFor(user: User, taskId: Id): Result[Id] = ctx.run(quote {
    query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id)).delete
  })

}
