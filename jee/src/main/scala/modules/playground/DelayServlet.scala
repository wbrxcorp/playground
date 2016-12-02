package modules.playground

import modules.scalatra.Success

class DelayServlet extends modules.scalatra.JsonSupport {
  get("/:millis") {
    Thread.sleep(params.getAs[Long]("millis").getOrElse(1000L))
    Success
  }
}
