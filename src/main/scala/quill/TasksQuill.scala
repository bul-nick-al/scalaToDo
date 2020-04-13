package quill

import models.{Id, Task, User}
import utils.{DatabaseConfig, Hasher}

object TasksQuill extends DatabaseConfig with Hasher {

  import ctx._

  def findAll: List[Task] = ctx.run(quote {
    query[Task]
  })

  def findAllFor(user: User): List[Task] = ctx.run(quote {
    query[Task].filter(_.userId == lift(user.id))
  })

  def findById(taskId: Id): Option[Task] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId))
    })
    .headOption

  def findByIdFor(user: User, taskId: Id): Option[Task] = ctx
    .run(quote {
      query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id))
    })
    .headOption

  def create(task: Task): Id =
    findUserById(task.userId) match {
      case Some(_) =>
        ctx.run(quote {
          query[Task].insert(lift(task)).returningGenerated(_.id)
        })
      case None => 0L
    }

  def update(newTask: Task): Id = ctx.run(
    quote {
      query[Task].filter(_.id == lift(newTask.id)).update(lift(newTask))
    }
  )

  def updateFor(user: User, newTask: Task): Id = ctx.run(quote {
    query[Task].filter(task => task.id == lift(newTask.id) && task.userId == lift(user.id)).update(lift(newTask))
  })

  def delete(taskId: Id): Id = ctx.run(quote {
    query[Task].filter(_.id == lift(taskId)).delete
  })

  def deleteFor(user: User, taskId: Id): Id = ctx.run(quote {
    query[Task].filter(task => task.id == lift(taskId) && task.userId == lift(user.id)).delete
  })

  def findByCredentials(login: String, password: String): Option[User] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login) && user.password == lift(md5(password)))
    })
    .headOption

  def findUserById(userId: Id): Option[User] = ctx
    .run(quote {
      query[User].filter(user => user.id == lift(userId))
    })
    .headOption

  def findUserByLogin(login: String): Option[User] = ctx
    .run(quote {
      query[User].filter(user => user.login == lift(login))
    })
    .headOption

  def create(user: User): Id =
    ctx.run(quote {
      query[User].insert(_.login -> lift(user.login), _.password -> lift(md5(user.password))).returningGenerated(_.id)
    })
}
