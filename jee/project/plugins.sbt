addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "4.0.1")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

libraryDependencies ++= {
  if (scala.util.Try(java.lang.Runtime.getRuntime.exec("javac -version").waitFor).map(x=>x == 0).getOrElse(false)) {
    Seq(Defaults.sbtPluginExtra("org.ensime" % "sbt-ensime" % "2.1.0", (sbtBinaryVersion in update).value, (scalaBinaryVersion in update).value))
  } else {
    println("JDK not installed. sbt-ensime is not loaded")
    Seq.empty
  }
}

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

//addSbtPlugin("com.arpnetworking" % "sbt-typescript" % "0.2.1")
