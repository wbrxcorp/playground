package modules.cms

import modules.scalatra.{Success,Failure}

class Api extends modules.scalatra.JsonSupport with modules.scalatra.LogErrors {
  get("/") {
    Map[String,Any](
      "version"->buildinfo.BuildInfo.version
    )
  }

  get("/login") {
    enrichSession(session).getAs[Int]("userId") match {
      case Some(userId) => Success(Map("userId"->userId, "username"->"shimarin"))
      case None => Failure
    }
  }

  post("/login") {
    val userId = 1
    session.setAttribute("userId", userId)
    Success(userId)
  }

  post("/logout") {
    enrichSession(session).getAs[Int]("userId") match {
      case Some(userId) =>
        session.removeAttribute("userId")
        Success(userId)
      case None => Failure
    }
  }
}
