package modules.movabletype

case class Blog(
  id:Int,
  name:String,
  blogClass:String,
  parent:Option[Blog]=None,
  children:Seq[Blog]=Nil
)
