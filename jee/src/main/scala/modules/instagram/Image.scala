package modules.instagram

case class Image(url:String,width:Int,height:Int)
case class Item(createdTime:org.joda.time.LocalDateTime,low:Option[Image],standard:Option[Image],thumbnail:Option[Image],caption:Option[String], link:Option[String])
