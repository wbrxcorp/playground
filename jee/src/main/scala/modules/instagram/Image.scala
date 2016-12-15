package modules.instagram

case class Image(url:String,width:Int,height:Int)
case class Item(createdTime:DateTime,low:Image,standard:Image,thumbnail:Image,caption:Option[String])
