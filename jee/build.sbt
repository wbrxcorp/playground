enablePlugins(BuildInfoPlugin)
//enablePlugins(JettyPlugin)
//org.ensime.EnsimeCoursierKeys.ensimeServerVersion in ThisBuild := "2.0.0-M3"

// http://stackoverflow.com/questions/25665848/how-to-load-setting-values-from-a-java-properties-file
val buildProperties = settingKey[java.util.Properties]("The build properties")

buildProperties := {
  val prop = new java.util.Properties()
  IO.load(prop, new File("project%sbuild.properties".format(System.getProperty("file.separator"))))
  prop
}

name := Option(buildProperties.value.getProperty("name")).getOrElse("playground")
scalaVersion in ThisBuild := Option(buildProperties.value.getProperty("scalaVersion")).getOrElse("2.12.4")
version := Option(buildProperties.value.getProperty("version")).getOrElse("0.20180117")
scalacOptions ++= Seq("-feature", "-deprecation")
mainClass in (Compile, run) := Some("WebAndSQLMain")
javaSource in Compile := scala.util.Try(java.lang.Runtime.getRuntime.exec("javac -version").waitFor).filter(_ == 0).map(x=>baseDirectory.value / "src" / "main" / "java").getOrElse(file("DOES/NOT/EXIST"))

//unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil) // java sources are for test only
//}
parallelExecution in Test := false

resolvers += "jitpack" at "https://jitpack.io"
resolvers += "clojars" at "http://clojars.org/repo/"
resolvers += "atlassian-3rd-party" at "https://packages.atlassian.com/maven-3rdparty/"
resolvers += "alfresco-public" at "https://artifacts.alfresco.com/nexus/content/repositories/public"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.12.4" // http://mvnrepository.com/artifact/org.scala-lang/scala-compiler
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.4" // http://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
libraryDependencies += "org.flywaydb" % "flyway-core" % "5.0.5" // http://mvnrepository.com/artifact/org.flywaydb/flyway-core
libraryDependencies += "commons-io" % "commons-io" % "2.6" // http://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "joda-time" % "joda-time" % "2.9.9"  // http://mvnrepository.com/artifact/joda-time/joda-time
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" // http://mvnrepository.com/artifact/ch.qos.logback/logback-classic
libraryDependencies += "com.typesafe.scala-logging" % "scala-logging_2.12" % "3.7.2" // https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging_2.12
libraryDependencies += "org.slf4j" % "slf4j-jdk14" % "1.7.25" // for tomcat: https://mvnrepository.com/artifact/org.slf4j/slf4j-jdk14
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.2"  // http://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "com.mashape.unirest" % "unirest-java" % "1.4.9" // http://mvnrepository.com/artifact/com.mashape.unirest/unirest-java
libraryDependencies += "com.opencsv" % "opencsv" % "3.8" // http://mvnrepository.com/artifact/com.opencsv/opencsv
libraryDependencies += "org.apache.velocity" % "velocity" % "1.7"  // http://mvnrepository.com/artifact/org.apache.velocity/velocity
libraryDependencies += "com.hubspot.jinjava" % "jinjava" % "2.3.4" // https://mvnrepository.com/artifact/com.hubspot.jinjava/jinjava
libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0" // http://mvnrepository.com/artifact/org.pegdown/pegdown
libraryDependencies += "com.vladsch.flexmark" % "flexmark-all" % "0.28.32" // https://mvnrepository.com/artifact/com.vladsch.flexmark/flexmark-java
libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.10.0.201712302008-r" // http://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
libraryDependencies += "com.yahoo.platform.yui" % "yuicompressor" % "2.4.8-rhino-alfresco-patched" // http://mvnrepository.com/artifact/com.yahoo.platform.yui/yuicompressor
libraryDependencies += "com.jakewharton.fliptables" % "fliptables" % "1.0.2" // https://mvnrepository.com/artifact/com.jakewharton.fliptables/fliptables
libraryDependencies += "com.typesafe" % "config" % "1.3.2" // http://mvnrepository.com/artifact/com.typesafe/config
libraryDependencies += "org.develnext.jphp" % "jphp-core" % "0.9.2" // https://github.com/jphp-compiler/jphp
libraryDependencies += "com.lihaoyi" % "pprint_2.12" % "0.5.3" // http://mvnrepository.com/artifact/com.lihaoyi/pprint_2.12
libraryDependencies += "com.m3" % "curly" % "0.5.6" // https://mvnrepository.com/artifact/com.m3/curly
libraryDependencies += "com.github.nscala-time" % "nscala-time_2.12" % "2.18.0" // http://mvnrepository.com/artifact/com.github.nscala-time/nscala-time_2.12
libraryDependencies += "net.lightbody.bmp" % "browsermob-core" % "2.1.5" // http://mvnrepository.com/artifact/net.lightbody.bmp/browsermob-core
libraryDependencies += "com.github.pathikrit" % "better-files_2.12" % "3.4.0" // http://mvnrepository.com/artifact/com.github.pathikrit/better-files_2.12

// JDBC drivers
libraryDependencies += "com.h2database" % "h2" % "1.4.196" // http://mvnrepository.com/artifact/com.h2database/h2
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6" // http://mvnrepository.com/artifact/mysql/mysql-connector-java
libraryDependencies += "com.oracle" % "ojdbc6" % "12.1.0.1-atlassian-hosted" // http://mvnrepository.com/artifact/com.oracle/ojdbc6
libraryDependencies += "com.microsoft.sqlserver" % "sqljdbc4" % "4.0" // http://mvnrepository.com/artifact/com.microsoft.sqlserver/sqljdbc4
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4" // http://mvnrepository.com/artifact/org.postgresql/postgresql

libraryDependencies ++= Seq(
  "scalikejdbc_2.12","scalikejdbc-syntax-support-macro_2.12" // http://mvnrepository.com/artifact/org.scalikejdbc/scalikejdbc_2.12
).map("org.scalikejdbc" % _ % "3.1.0")

libraryDependencies ++= Seq(
  "scalatra_2.12", "scalatra-json_2.12"
).map("org.scalatra" % _ % "2.6.2") // http://mvnrepository.com/artifact/org.scalatra/scalatra_2.12

libraryDependencies ++= Seq(
  "json4s-jackson_2.12", "json4s-ext_2.12"
).map("org.json4s" % _ % "3.5.3") // http://mvnrepository.com/artifact/org.json4s/json4s-jackson_2.12

libraryDependencies ++= Seq(
  "poi", "poi-ooxml"
).map("org.apache.poi" % _ % "3.17")  // http://mvnrepository.com/artifact/org.apache.poi/poi

libraryDependencies ++= Seq(
  "jetty-webapp","jetty-plus","jetty-annotations","jetty-servlets"
).map("org.eclipse.jetty" % _ % "9.3.22.v20171030") // http://mvnrepository.com/artifact/org.eclipse.jetty/jetty-webapp

libraryDependencies ++= Seq(
  "trireme-core", // https://mvnrepository.com/artifact/io.apigee.trireme/trireme-core
  "trireme-node10src" // https://mvnrepository.com/artifact/io.apigee.trireme/trireme-node10src
).map("io.apigee.trireme" % _ % "0.9.0")

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test // http://mvnrepository.com/artifact/com.novocode/junit-interface
libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.4" % Test // http://mvnrepository.com/artifact/org.scalatest/scalatest_2.12
libraryDependencies ++= Seq(
  "scalatra-test_2.12", "scalatra-scalatest_2.12"
).map("org.scalatra" % _ % "2.6.2" % Test) // http://mvnrepository.com/artifact/org.scalatra/scalatra-scalatest_2.12

libraryDependencies ++= Seq(
  "selenium-support", // http://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-support
  "selenium-chrome-driver"  // http://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-chrome-driver
).map("org.seleniumhq.selenium" % _ % "3.8.1" % Test)

val e2etest = taskKey[Unit]("Execute e2e.[projectname].* tests")
e2etest := (Def.taskDyn {
  val nameStr = name.value
  Def.task {
    (testOnly in Test).toTask(s" e2e.${nameStr}.*").value
  }
}).value

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
mainClass in assembly := Some("ConsoleMain")
test in assembly := {}

initialCommands in console := "if (modules.console.isRunFromSBT) modules.config.loadConfig(Option(System.getProperty(\"profile\")).getOrElse(\"standalone\"));" + (((new File(".") / "src/main/scala/modules") * "*" * "package.scala"):PathFinder).get.map("import modules.`" + _.getParentFile.getName + "`._").mkString(";") + ";initDefaultDatabase;migrateDefaultDatabaseIfNecessary;import scalikejdbc._"

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion,initialCommands in console) //buildInfoPackage := "buildinfo"
