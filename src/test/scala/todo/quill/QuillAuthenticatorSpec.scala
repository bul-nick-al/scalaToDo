package todo.quill

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.directives.Credentials
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import monix.execution.Scheduler.Implicits.global
import todo.mock
import todo.mock.DatabaseConfigMock
import todo.models.User
import todo.utils.Md5Hasher

class QuillAuthenticatorSpec extends AnyFlatSpec with ScalaFutures with Matchers{

  val authenticator = new QuillAuthenticator(new UserQuill(new DatabaseConfigMock {}, new Md5Hasher), new Md5Hasher)

  "QuillAuthenticator" should "return a user correctly if their credentials match some stored in db" in {
    val result = authenticator.authenticate(Credentials(Some(BasicHttpCredentials("nick", "123"))))
    whenReady(result) { res: Option[User] => res shouldEqual Some(mock.dbUser) }
  }

  it should "return None if there is no such user in the db" in {
    val result = authenticator.authenticate(Credentials(Some(BasicHttpCredentials("kek", "123"))))
    whenReady(result) { res: Option[User] => res shouldEqual None }
  }
}
