package modules.hash

object Module extends modules.Module {
  def sha1(in:java.io.InputStream):String = modules.hash.sha1(in)
  def sha1(str:String,charset:String="UTF-8"):String = modules.hash.sha1(str)
}
