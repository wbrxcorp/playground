package modules.playground

import modules.scalatra.Success
import org.json4s.jackson.JsonMethods

class FormServlet extends modules.scalatra.JsonSupport with org.scalatra.servlet.FileUploadSupport {
  configureMultipartHandling(org.scalatra.servlet.MultipartConfig(maxFileSize = Some(3*1024*1024L), fileSizeThreshold = Some(1*1024*1024)))

  post("/withFiles") {
    import org.json4s.JsonDSL._
    logger.info("Posted values in JSON format:" + JsonMethods.pretty(JsonMethods.render(params)))
    val files = fileParams.map { case (name, value) =>
      (name, Map("filename"->value.getName, "size"->value.getSize.toString, "contentType"->value.getContentType.getOrElse("Unknown")))
    }.toMap
    logger.info("Posted files in JSON format:" + JsonMethods.pretty(JsonMethods.render(files)))
    Success
  }
}
