package modules.movabletype

case class Category(
  id:Int,
  blogId:Int,
  label:String,
  categoryClass:String,
  parent:Option[Int]=None,
  children:Seq[Category]=Nil
)
