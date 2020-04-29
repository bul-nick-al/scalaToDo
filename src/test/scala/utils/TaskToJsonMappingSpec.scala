package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.utils.TaskToJsonMapping
import spray.json._
import todo.mock

class TaskToJsonMappingSpec extends AnyFlatSpec with Matchers {

  val mapping = new TaskToJsonMapping {}
  import mapping._

  "mapper" should "parse a User object to JSON correctly" in {
     mock.user1.toJson.toString() shouldBe "{\"id\":1,\"login\":\"kek1\",\"password\":\"chebrek1\"}"
  }

  it should "parse a Task object to JSON correctly" in {
    mock.task1.toJson.toString() shouldBe "{\"completed\":false,\"description\":\"do task 1\",\"id\":1,\"title\":\"task1\",\"userId\":1}"
  }
}
