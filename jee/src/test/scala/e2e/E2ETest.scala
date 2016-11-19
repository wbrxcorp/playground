package e2e

trait E2ETest extends org.scalatest.FlatSpec with org.scalatest.selenium.WebBrowser with org.scalatest.BeforeAndAfterAll {
  modules.config.loadConfig("standalone")
  val driverPath = if (System.getProperty("os.name").startsWith("Windows")) {
    "bin/chromedriver.exe";
  } else {
    "bin/chromedriver";
  }
  if (new java.io.File(driverPath).isFile) {
    System.setProperty("webdriver.chrome.driver", driverPath)
  }

  implicit val webDriver: org.openqa.selenium.WebDriver = new org.openqa.selenium.chrome.ChromeDriver
  var server:org.eclipse.jetty.server.Server = _
  var port:Int = _

  def URL = "http://localhost:%d".format(this.port)

  override def beforeAll() = {
    val (server, port) = modules.jetty.startServer
    this.server = server
    this.port = port
  }

  override def afterAll() = {
    close
    server.stop
  }
}
