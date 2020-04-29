package todo

import todo.models.User
import todo.models._

package object mock {
  val user1: User = User(1, "kek1", "chebrek1")
  val user2: User = User(20, "kek2", "chebrek2")
  val user3: User = User(3, "kek3", "chebrek3")
  val user4: User = User(4, "kek4", "chebrek4")

  val dbUser: User = User(1, "nick", "202cb962ac59075b964b07152d234b70")

  val listOfUsers = List(user1, user2, user3, user4)

  val task1: Task = Task(1, "task1", "do task 1", 1, completed = false)
  val task2: Task = Task(2, "task2", "do task 2", 1, completed = true)
  val task3: Task = Task(3, "task3", "do task 3", 1, completed = true)
  val task4: Task = Task(4, "task4", "do task 4", 1, completed = false)
  val task5: Task = Task(5, "task5", "do task 5", 1, completed = false)

  val listOfTasks = List(task1, task2, task3, task4, task5)
}
