package modules.movabletype

case class Entry (
  id:Int,
  blogId:Int,
  categoryId:Option[Int],
  entryClass:Option[String],
  keywords:Option[String]=None,
  status:Int,
  templateId:Option[Int]=None,
  text:Option[String]=None,
  textMore:Option[String]=None,
  title:Option[String]=None,
  unpublishedOn:Option[java.time.LocalDateTime]=None
)
