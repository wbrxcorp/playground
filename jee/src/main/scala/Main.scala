import sun.misc.Signal

object Main extends App {
  modules.config.loadConfig(args.toSeq.headOption.getOrElse("standalone"))
  val config = modules.config.get
  val port = config.webAppPort
  val server = modules.webapp.startWebApp(port, config.webAppDirs)
  println("http://localhost:%d".format(port))
  println("Ctrl+C to stop running")
  scala.util.Try(Signal.handle(new Signal("INT"), new sun.misc.SignalHandler {
    def handle(sig:Signal):Unit = server.stop
  }))

}
