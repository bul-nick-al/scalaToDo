package todo.quill

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.mock
import todo.mock.DatabaseConfigMock
import todo.models.{Task, User}
import todo.utils.Md5Hasher
import todo.models._

class UserQuillSpec extends AnyFlatSpec with ScalaFutures with Matchers{

  val dbConfig = new DatabaseConfigMock {}
  val hasher = new Md5Hasher

  val userQuill = new UserQuill(dbConfig, hasher)

  "User quill" should "find a user given their credentials" in {
    val result = userQuill.findByCredentials("nick", "123")
    whenReady(result) { res => res shouldEqual Some(mock.dbUser) }
  }

  it should "return None if there is no such user" in {
    val result = userQuill.findByCredentials("kek", "123")
    whenReady(result) { res => res shouldEqual None }
  }

  it should "find a user given their id" in {
    val result = userQuill.findUserById(1)
    whenReady(result) { res => res shouldEqual Some(mock.dbUser) }
  }

  it should "find a user given their login" in {
    val result = userQuill.findUserByLogin("nick")
    whenReady(result) { res => res shouldEqual Some(mock.dbUser) }
  }

  it should "create a user" in {
    whenReady(userQuill.findUserByLogin(mock.user3.login)) { res =>
      if (res.isEmpty) {
        val result = userQuill.create(mock.user3)
        whenReady(result) { _ =>
          whenReady(userQuill.findUserById(mock.user3.id)) {
            _ shouldEqual mock.user3
          }
        }
      }
    }
  }
}
