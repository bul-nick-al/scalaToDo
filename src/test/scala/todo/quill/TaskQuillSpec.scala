package todo.quill
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.mock
import todo.mock.DatabaseConfigMock
import todo.models.{Task, User}
import todo.utils.Md5Hasher

class TaskQuillSpec extends AnyFlatSpec with ScalaFutures with Matchers{

  val dbConfig = new DatabaseConfigMock {}
  val hasher = new Md5Hasher

  val tasksQuill = new TasksQuill(dbConfig, new UserQuill(dbConfig, hasher), hasher)

  "Task quill" should "find all tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser)
    whenReady(result) { res: List[Task] => res.map(_.userId shouldEqual mock.dbUser.id) }
  }

  it should "return empty lift for a not existing user" in {
    val result = tasksQuill.findAllFor(mock.user2)
    whenReady(result) { res: List[Task] => res.length shouldEqual 0 }
  }

  it should "find all completed tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser, completed = true)
    whenReady(result) { res: List[Task] => res.map(_.completed shouldEqual true) }
  }

  it should "find all not completed tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser, completed = false)
    whenReady(result) { res: List[Task] => res.map(_.completed shouldEqual false)}
  }

  it should "update a task for a user and return 1" in {
    import todo.models._
    val task = Task(2, "buy food2 amend", "buy some food2", 1, completed = true)
    val result = tasksQuill.update(task)
    whenReady(result) { res =>
      res shouldEqual 1
      whenReady(tasksQuill.findByIdFor(mock.dbUser, task.id)) { res2 =>
        res2 shouldEqual Some(task)
      }
    }
  }

  it should "return 0 if one tries to create a task for a not existing user" in {
    import todo.models._
    val task = Task(0, "a", "b", 2, completed = false)
    val result = tasksQuill.create(task)
    whenReady(result) { res => res shouldEqual 0}
  }

  it should "delete a task for a user and return its id" in {
    import todo.models._
    val result = tasksQuill.deleteFor(mock.dbUser, 1)
    whenReady(result) { _ =>
      whenReady(tasksQuill.findByIdFor(mock.dbUser, 1)) { res2 =>
        res2 shouldEqual None
      }
    }
  }
}
