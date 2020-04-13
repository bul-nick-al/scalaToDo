package utils

trait Hasher {
  def md5(s: String): String = {
    import java.security.MessageDigest
    import java.math.BigInteger
    val md           = MessageDigest.getInstance("MD5")
    val digest       = md.digest(s.getBytes)
    val bigInt       = new BigInteger(1, digest)
    val hashedString = bigInt.toString(16)
    hashedString
  }
}
