package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import todo.utils.Md5Hasher

class HasherSpec extends AnyFlatSpec with Matchers {
  val hasher = new Md5Hasher()

  "the md5 hasher" should "produce an md5 hash of a string" in {
    hasher.hash("123") shouldEqual "5f4dcc3b5aa765d61d8327deb882cf99"
  }
}
