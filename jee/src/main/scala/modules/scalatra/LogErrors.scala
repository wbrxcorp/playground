package modules.scalatra

trait LogErrors extends org.scalatra.ScalatraServlet with com.typesafe.scalalogging.LazyLogging {
  error { case e =>
    logger.error("error", e)
    throw e
  }
}
