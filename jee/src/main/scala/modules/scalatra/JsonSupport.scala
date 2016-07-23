package modules.scalatra

import org.json4s._
import org.scalatra.BadRequest

trait JsonSupport extends org.scalatra.ScalatraServlet with org.scalatra.json.JacksonJsonSupport with com.typesafe.scalalogging.slf4j.LazyLogging {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats.withBigDecimal ++ org.json4s.ext.JodaTimeSerializers.all

  def withJsonObject[T](obj:org.json4s.JValue)(f:JObject=>T):T = obj match {
    case x:JObject =>
      logger.debug(x.toString)
      try {
        f(x)
      }
      catch {
        case e:Throwable =>
          logger.debug("error parsing json object", e)
          halt(BadRequest(e))
      }
    case _ => halt(BadRequest("Request body must be a json object"))
  }

  error { case e =>
    logger.error("error", e)
    throw e
  }

  def withJsonRequest[T](f:JObject=>T):T = withJsonObject(parsedBody)(f)

  notFound {
    contentType = "application/json"
    org.scalatra.NotFound(Map("code"->404,"message"->"Route Not Found"))
  }
}
