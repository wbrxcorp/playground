import sun.misc.Signal

object WebMain extends App {
  // 第1引数をプロファイル名として使用、省略時は "standalone" (modules.config.DefaultStandaloneConfig 又は profiles.standalone.Config)
  modules.config.loadConfig(args.toSeq.headOption.getOrElse("standalone"))

  val (server, port) = modules.jetty.startServer
  println("http://localhost:%d".format(port))
  println("Ctrl+C to stop running")
  scala.util.Try(Signal.handle(new Signal("INT"), new sun.misc.SignalHandler {
    def handle(sig:Signal):Unit = server.stop
  }))
}

object ConsoleMain extends App {
  modules.config.loadConfig(args.toSeq.headOption.getOrElse("default"))
  modules.console.runConsole
}

object WebAndSQLMain extends App {
  modules.config.loadConfig(args.toSeq.headOption.getOrElse("standalone"))
  val (server, port) = modules.jetty.startServer
  println("http://localhost:%d".format(port))

  val config = modules.config.get
  modules.common.using(java.sql.DriverManager.getConnection(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)) { conn =>
    (new org.h2.tools.Shell).runTool(conn)
  }
  server.stop
}
