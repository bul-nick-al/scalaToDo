package todo.models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.utils.Md5Hasher

class TaskSpec extends AnyFlatSpec with Matchers{

  "Task constructor" should "initialise a Task object correctly" in {
    val task = Task(1, "Title", "Description", 2, completed = true)
    task.id shouldEqual 1
    task.title shouldEqual "Title"
    task.description shouldEqual  "Description"
    task.userId shouldEqual 2
    task.completed shouldEqual true
  }
}
