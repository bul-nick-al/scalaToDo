package todo.quill
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import todo.mock
import todo.mock.DatabaseConfigMock
import todo.models.{Task, User}
import todo.utils.Md5Hasher

class TaskQuillSpec extends AnyFlatSpec with ScalaFutures with Matchers{

  val timeout: PatienceConfiguration.Timeout = timeout(Span(6, Seconds))

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  val dbConfig = new DatabaseConfigMock {}
  val hasher = new Md5Hasher

  val tasksQuill = new TasksQuill(dbConfig, new UserQuill(dbConfig, hasher), hasher)

  "Task quill" should "find all tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser)
    whenReady(result, timeout) { res: List[Task] => res.map(_.userId shouldEqual mock.dbUser.id) }
  }

  it should "return empty list for a not existing user" in {
    val result = tasksQuill.findAllFor(mock.user2)
    whenReady(result, timeout) { res: List[Task] => res.length shouldEqual 0 }
  }

  it should "find all completed tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser, completed = true)
    whenReady(result, timeout) { res: List[Task] => res.map(_.completed shouldEqual true) }
  }

  it should "find all not completed tasks for a given user" in {
    val result = tasksQuill.findAllFor(mock.dbUser, completed = false)
    whenReady(result, timeout) { res: List[Task] => res.map(_.completed shouldEqual false)}
  }

  it should "update a task for a user and return 1" in {
    import todo.models._
    val task = Task(2, "buy food2 amend", "buy some food2", 1, completed = true)
    val result = tasksQuill.update(task)
    whenReady(result, timeout) { res =>
      res shouldEqual 1
      whenReady(tasksQuill.findByIdFor(mock.dbUser, task.id), timeout) { res2 =>
        res2 shouldEqual Some(task)
      }
    }
  }

  it should "return 0 if one tries to create a task for a not existing user" in {
    import todo.models._
    val task = Task(0, "a", "b", 20, completed = false)
    val result = tasksQuill.create(task)
    whenReady(result, timeout) { res => res shouldEqual 0}
  }

  it should "delete a task for a user and return its id" in {
    import todo.models._
    val result = tasksQuill.deleteFor(mock.dbUser, 1)
    whenReady(result, timeout) { _ =>
      whenReady(tasksQuill.findByIdFor(mock.dbUser, 1), timeout) { res2 =>
        res2 shouldEqual None
      }
    }
  }
}
