package modules.fakephp

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.apache.commons.io.IOUtils

class FakePHPServlet extends javax.servlet.http.HttpServlet with com.typesafe.scalalogging.slf4j.LazyLogging {
  //val regex = """<!--\s*#include\s+virtual=["'](.*)["']\s*-->""".r
  val regex = """(?s)<\?php\s+if\s*\(\s*isset\(\$\_SESSION\[\"(.*?)\"\]\)\s*\)\s*\{\s*\?>(.*?)<\?php\s+\}\s+else\s+\{\s*\?>(.*?)<\?php\s+\}\s*\?>""".r

  def loadResource(path:String):String = {
    Option(getServletContext.getResource(path)).map { resource =>
      val stream = resource.openStream()
      try {
        IOUtils.toString(stream, "utf-8")
      }
      finally {
        stream.close
      }
    }.getOrElse("")
  }

  private def resolvePHP(request:HttpServletRequest, php:String):String = {
    regex.replaceAllIn(php, { m =>
      val sessionVariableName = m.group(1)
      val replacement = Option(request.getSession.getAttribute(sessionVariableName)) match {
        case Some(x) =>
          logger.debug("session.getAttribute(\"%s\") = %s".format(sessionVariableName, x))
          m.group(2) // session exists
        case None =>
          logger.debug("session \"%s\" does not exist".format(sessionVariableName))
          m.group(3) // session does not exist
      }
      resolvePHP(request, scala.util.matching.Regex.quoteReplacement(replacement))
    })
  }

  override def doGet(request:HttpServletRequest, response:HttpServletResponse):Unit = {
    //println(request.getServletPath)
    Option(getServletContext.getResource(request.getServletPath)) match {
      case Some(resource) =>
        val stream = resource.openStream()
        response.setContentType("text/html")
        response.setCharacterEncoding("utf-8")
        val php = resolvePHP(request, IOUtils.toString(stream, "utf-8"))
        IOUtils.write(php, response.getOutputStream, "utf-8")
        stream.close
      case None =>
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }
  }

}
