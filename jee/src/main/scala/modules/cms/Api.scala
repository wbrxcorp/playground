package modules.cms

import org.json4s.{Formats, JValue}

case class Result(success:Boolean, info:Option[Any] = None)
object Success extends Result(true, None) {
  def apply[T](info:T):Result = Result(true, Some(info))
}
object Failure extends Result(false, None) {
  def apply[T](info:T):Result = Result(false, Some(info))
}

class ResultSerializer extends org.json4s.Serializer[Result] {
  def deserialize(implicit format:Formats): PartialFunction[(org.json4s.TypeInfo, JValue), Result] = {
    throw new org.json4s.MappingException("Deserializing Result class is not supporeted")
  }

  def serialize(implicit formats:Formats): PartialFunction[Any, JValue] = {
    case x:Result =>
      import org.json4s.JsonDSL._
      ("success" -> x.success) ~ ("info" -> org.json4s.Extraction.decompose(x.info))
  }
}

class Api extends modules.scalatra.JsonSupport with modules.scalatra.LogErrors {
  override protected implicit def jsonFormats: org.json4s.Formats = super.jsonFormats + new ResultSerializer

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
