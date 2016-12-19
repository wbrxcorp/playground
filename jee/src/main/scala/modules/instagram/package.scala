package modules

// https://www.instagram.com/shimariso/media/

package object instagram {
  import com.m3.curly.scala._
  //import com.github.nscala_time.time.Imports._
  import org.json4s._
  //import org.json4s.{JField,JLong,JString,JArray,JObject}
  //import org.json4s.jackson.JsonMethods._

  //import modules.curly._
  private implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all

  def getLatestImages(username:String):Seq[Item] = {
    val response = HTTP.get("https://www.instagram.com/%s/media/".format(java.net.URLEncoder.encode(username, "UTF-8")))

    for {
      JArray(items) <- org.json4s.jackson.JsonMethods.parse(response.asString) \ "items"
      item <- items
    } yield Item(
      new org.joda.time.LocalDateTime( (item \ "created_time").extractOpt[String].map(_.toLong * 1000L).getOrElse(System.currentTimeMillis)),
      (item \ "images" \ "low_resolution").extractOpt[Image],
      (item \ "images" \ "standard_resolution").extractOpt[Image],
      (item \ "images" \ "thumbnail").extractOpt[Image],
      (item \ "caption" \ "text").extractOpt[String],
      (item \ "link").extractOpt[String]
    )
  }
}
