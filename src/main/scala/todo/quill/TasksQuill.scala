package todo.quill

import com.mysql.cj.protocol.ResultListener
import todo.models.{Id, Task, User}
import todo.utils.{DatabaseConfig, Hasher}

class TasksQuill(val dbConfig: DatabaseConfig, hasher: Hasher) {

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
    findUserById(task.userId).flatMap {
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

  def findByCredentials(login: String, password: String): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login) && user.password == lift(hasher.hash(password)))
    })
    .map(_.headOption)

  def findUserById(userId: Id): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.id == lift(userId))
    })
    .map(_.headOption)

  def findUserByLogin(login: String): Result[Option[User]] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login))
    })
    .map(_.headOption)

  def create(user: User): Result[Id] =
    ctx.run(quote {
      query[User].insert(_.login -> lift(user.login), _.password -> lift(hasher.hash(user.password))).returningGenerated(_.id)
    })
}
