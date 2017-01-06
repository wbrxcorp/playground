package modules

// https://github.com/pathikrit/better-files
// http://takezoe.hatenablog.com/entry/2016/01/27/155542

package object betterfiles {
  import better.files._

  def loadBuildSbt:File = {
    file"build.sbt"
  }

  def diffOfTwoFiles(filename1:String, filename2:String):Seq[String] = {
    (File(filename1).lines.toSet diff File(filename2).lines.toSet).toSeq.sorted
  }
}
