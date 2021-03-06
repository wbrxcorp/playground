package e2e

import net.lightbody.bmp.util.{HttpMessageInfo,HttpMessageContents}
import net.lightbody.bmp.filters.{RequestFilter,ResponseFilter}
import io.netty.handler.codec.http.{HttpRequest, HttpResponse}

trait E2ETest extends org.scalatest.FlatSpec with org.scalatest.selenium.WebBrowser with org.scalatest.BeforeAndAfterAll with RequestFilter with ResponseFilter{
  import org.openqa.selenium.remote.{DesiredCapabilities,CapabilityType}
  import org.openqa.selenium.chrome.ChromeOptions
  import net.lightbody.bmp.BrowserMobProxyServer

  implicit var webDriver: org.openqa.selenium.remote.RemoteWebDriver = _

  var server:org.eclipse.jetty.server.Server = _
  var proxy:BrowserMobProxyServer = _
  var port:Int = _

  def URL = "http://localhost:%d".format(this.port)

  def setupProxy(proxy:BrowserMobProxyServer):Unit = { }

  def filterRequest(request:HttpRequest, contents:HttpMessageContents, messageInfo:HttpMessageInfo):HttpResponse = {
    //println(">> " + messageInfo.getOriginalUrl)
    null // null以外をレスポンスすると即時にレスポンスとしてその値が使われる
  }

  def filterResponse(response:HttpResponse, contents:HttpMessageContents, messageInfo:HttpMessageInfo):Unit = {
    //println("<< " + messageInfo.getOriginalUrl)
  }

  override def beforeAll() = {
    modules.config.loadConfig("standalone")

    // Webサーバの起動
    val (server, port) = modules.jetty.startServer(Some(0))
    this.server = server
    this.port = port

    // ローカルProxyサーバの起動
    this.proxy = new BrowserMobProxyServer
    this.proxy.addRequestFilter(this)
    this.proxy.addResponseFilter(this)
    this.setupProxy(this.proxy)

    proxy.start(0)
    val capabilities = DesiredCapabilities.chrome
    val chromeOptions = new ChromeOptions
    //chromeOptions.addArguments("headless")
    chromeOptions.addArguments("disable-gpu")
    chromeOptions.addArguments("no-sandbox")
    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions)
    capabilities.setCapability(CapabilityType.PROXY, net.lightbody.bmp.client.ClientUtil.createSeleniumProxy(this.proxy));

    // WebDriverのセットアップ
    val driverPath = if (System.getProperty("os.name").startsWith("Windows")) {
      "bin/chromedriver.exe";
    } else {
      "bin/chromedriver";
    }
    if (new java.io.File(driverPath).isFile) {
      System.setProperty("webdriver.chrome.driver", driverPath)
    }
    this.webDriver = Option(System.getProperty("selenium.server.remote.url")) match {
      case Some(url) => new org.openqa.selenium.remote.RemoteWebDriver(new java.net.URL(url), capabilities)
      case None => new org.openqa.selenium.chrome.ChromeDriver(capabilities)
    }
  }

  override def afterAll() = {
    quit
    proxy.stop
    server.stop
  }
}
