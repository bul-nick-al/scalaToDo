package todo.models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserSpec extends AnyFlatSpec with Matchers{

  "User constructor" should "initialise a User object correctly" in {
    val user = User(1, "name", "password")
    user.id shouldEqual 1
    user.login shouldEqual "name"
    user.password shouldEqual "password"
  }
}

