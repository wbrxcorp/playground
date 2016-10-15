package modules.crawler

case class Options(
  trustCanonicalURL:Boolean = false,
  customHeaders:Seq[org.apache.http.message.BasicHeader] = Nil,
  cacheTTL:Int = -1 /*infinite*/
)
