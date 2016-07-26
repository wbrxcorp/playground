package modules

package object file {
  def isdir(path:String):Boolean = new java.io.File(path).isDirectory
  def mapEachLine[T](path:String)(f:String=>T):Seq[T] = {
    // TBD
    Seq()
  }
}
