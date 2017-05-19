package modules

import org.eclipse.jetty.server.{Server, ServerConnector}

package object jetty {
  def startWebApp(port:Int, resources:Array[String]):Server = {
    val root = new org.eclipse.jetty.webapp.WebAppContext
    root.setBaseResource(new org.eclipse.jetty.util.resource.ResourceCollection(resources))
    root.setContextPath("/")
    root.setClassLoader(new java.net.URLClassLoader(new Array[java.net.URL](0), this.getClass().getClassLoader()))
    root.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false")

    // enable CGI
    val servletHolder = new org.eclipse.jetty.servlet.ServletHolder
    servletHolder.setClassName("org.eclipse.jetty.servlets.CGI")
    servletHolder.setAsyncSupported(true)
    root.addServlet(servletHolder, "*.cgi")

    val server = new Server(port)
    server.setHandler(root)
    server.start
    server
  }

  def startWebApp(port:Int, resource:String):Server = startWebApp(port, Array(resource))

  def startServer(port:Option[Int] = None):(Server, Int) = {
    val config = modules.config.get
    val server = startWebApp(port.getOrElse(config.webAppPort), config.webAppDirs)
    (server, server.getConnectors()(0).asInstanceOf[ServerConnector].getLocalPort())
  }

  def startServer:(Server, Int) = startServer(None)

}
