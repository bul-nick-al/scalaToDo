package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.utils.Md5Hasher

class HasherSpec extends AnyFlatSpec with Matchers {
  val hasher = new Md5Hasher()

  "the md5 hasher" should "produce an md5 hash of a string" in {
    hasher.hash("123") shouldEqual "202cb962ac59075b964b07152d234b70"
  }
}
