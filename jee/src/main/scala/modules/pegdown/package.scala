package modules

import org.pegdown.LinkRenderer

package object pegdown {
  def renderMarkdown(markdown:String, linkRenderer:Option[LinkRenderer]=None):String = {
    val processor = new org.pegdown.PegDownProcessor(org.pegdown.Extensions.ALL)
    linkRenderer match {
      case Some(linkRenderer) => processor.markdownToHtml(markdown, linkRenderer)
      case None => processor.markdownToHtml(markdown)
    }
  }

  def renderMarkdown(markdown:String, linkRenderer:LinkRenderer):String = renderMarkdown(markdown, Some(linkRenderer))
}
