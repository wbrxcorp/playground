package modules

import scala.collection.JavaConverters._
import _root_.scalikejdbc.{DBSession, SQLInterpolation}
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup

package object crawler extends SQLInterpolation with com.typesafe.scalalogging.slf4j.LazyLogging {
  import modules.common.using
  import modules.hash.sha1
  val userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36"

  private def detectCharset(contentType:String):Option[String] = {
    val charsetPart = """.+;\s*charset\s*=\s*(.+)""".r
    contentType match {
      case charsetPart(x) =>
        println("charset detected:" + x)
        Some(x)
      case _ => None
    }
  }

  private def convertToUTF8(rawstr:Array[Byte], charset:String):String = {
     val decoder = java.nio.charset.Charset.forName(charset).newDecoder;
     decoder.onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE);
     decoder.onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE);
     val bb = java.nio.ByteBuffer.wrap(rawstr)
     val parsed = decoder.decode(bb)
     parsed.toString
  }

  def fetchURL(url:String, options:Options=Options())(implicit httpClient:CloseableHttpClient):Either[Int,(String/*canonicalURL*/,String/*content*/)] = {
    val request = new HttpGet(url)
    options.customHeaders.foreach(request.addHeader(_))
    val context = org.apache.http.client.protocol.HttpClientContext.create
    using(httpClient.execute(request, context)) { response =>
      response.getStatusLine.getStatusCode match {
        case 200 =>
          val lastRedirectedURL = Option(context.getRedirectLocations).map(_.asScala.lastOption.map(_.toString)).flatten.getOrElse(url)
          val contentBytes = IOUtils.toByteArray(response.getEntity.getContent)
          val charset = detectCharset(response.getEntity.getContentType.getValue).orElse {
            val tmpSoup = Jsoup.parse(new String(contentBytes, "iso-8859-1"))
            //<meta http-equiv="Content-Type" content="text/html; charset=big5" />
            Option(tmpSoup.select("meta[http-equiv=Content-Type]").first).map { meta =>
              detectCharset(meta.attr("content"))
            }.flatten
          }.getOrElse("UTF-8")
          val content = convertToUTF8(contentBytes,  charset)
          val canonicalURL = (if (options.trustCanonicalURL) {
            val body = Jsoup.parse(content, lastRedirectedURL)
            Option(body.select("head > link[rel=canonical]").first).map(_.attr("href")).orElse(
              Option(body.select("meta[property=og:url]").first).map(_.attr("content"))
            )
          } else { None }).getOrElse(lastRedirectedURL)
          Right((canonicalURL, content))
        case code:Int => Left(code)
      }
    }
  }

  private def fetchURLFromCache(url:String, ttl:Int = -1)(implicit session:DBSession):Option[(String/*actualURL*/,String/*content*/)] = {
    if (ttl == 0) return None // TTL=0はキャッシュ無効の合図
    val ttlCondition = if (ttl > 0) sqls"and created_at >= TIMESTAMPADD(SECOND,${-ttl},now())" else sqls"" /*TTLが負の数ならキャッシュは無期限*/
    sql"select url,canonical_url_id,content from urls where id=${sha1(url)} ${ttlCondition}".map { row =>
      (row.string(1), row.stringOpt(2), row.stringOpt(3))
    }.single.apply.flatMap { case (url, canonicalURLId, content) =>
      (canonicalURLId, content) match {
        case (Some(canonicalURLId), None) => fetchURLFromCache(url, ttl)
        case (None, Some(content)) => Some((url, content))
        case _ => None
      }
    }
  }

  def getPage(url:String, options:Options=Options())(implicit session:DBSession, httpClient:CloseableHttpClient):Either[Int, org.jsoup.nodes.Document] = {
    fetchURLFromCache(url, options.cacheTTL).orElse {
      fetchURL(url, options).right.map(Some(_)) match { // todo: mapを使ったもっとましな方法にできるはず
        case Right(x) => Some(x)
        case Left(code) => return Left(code)
      }
    }.map { case (canonicalURL:String, content:String) =>
      Right(Jsoup.parse(content, canonicalURL))
    }.getOrElse(Left(404))
  }
}
