package modules

package object hash {
  import java.security.MessageDigest

  def sha1(bytes:Array[Byte]):String = {
    org.apache.commons.codec.binary.Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(bytes))
  }
  def sha1(in:java.io.InputStream):String = {
    sha1(org.apache.commons.io.IOUtils.toByteArray(in))
  }
  def sha1(str:String, charset:String = "UTF-8"):String = {
    sha1(str.getBytes(charset))
  }

  def sha256(str:String):String = {
    val buf = new StringBuffer()
    val md = MessageDigest.getInstance("SHA-256")
    md.update(str.getBytes())
    md.digest().foreach { b =>
      buf.append("%02x".format(b))
    }
    buf.toString
  }

  def comparePassword(encrypted:String, password:String):Boolean = {
    encrypted.split('$') match {
      case x if x.length == 2 => x(1) == sha256("%s%s".format(x(0), password))
      case _ => false
    }
  }

  def encryptPassword(password:String):String = {
    val salt = new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(5).mkString
    "%s$%s".format(salt, sha256("%s%s".format(salt, password)))
  }
}
