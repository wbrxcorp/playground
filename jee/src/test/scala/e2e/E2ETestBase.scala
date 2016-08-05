package e2e

import org.junit.{Test,Before,After,BeforeClass,AfterClass}

class E2ETestBase extends org.scalatest.selenium.Chrome with com.typesafe.scalalogging.slf4j.LazyLogging {
  // http://www.scalatest.org/user_guide/using_selenium
  def root:String = "http://localhost:%d/".format(E2ETestBase.port)

  @Before def before():Unit = go to root
  @After def after():Unit = close
}

object E2ETestBase {
  var server:org.eclipse.jetty.server.Server = _
  var port:Int = _

  modules.config.loadConfig("standalone")

  val driverPath =
    if (System.getProperty("os.name").startsWith("Windows")) "bin/chromedriver.exe"
    else "bin/chromedriver"

  if (new java.io.File(driverPath).isFile) {
    System.setProperty("webdriver.chrome.driver", driverPath)
  }

  @BeforeClass
  def beforeClass():Unit = {
    val (server, port) = modules.webapp.startServer
    this.server = server
    this.port = port
  }

  @AfterClass def afterClass():Unit = server.stop
}

class SampleE2ETest extends E2ETestBase {
  @Test def testBar():Unit = {
    go to root + "markdown/"
  }
}
