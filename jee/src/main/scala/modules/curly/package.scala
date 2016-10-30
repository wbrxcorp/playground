package modules

// https://github.com/m3dev/curly
package object curly {
  import com.m3.curly.scala._

  def curly_get_example:Unit = {
    //val response = HTTP.get("http://search.example.com?query=Application&lang=Scala")

    val response = HTTP.get("http://search.example.com", "query" -> "Application", "lang" -> "Scala")

    val status: Int = response.status
    val headers: Map[String, String] = response.headers
    val headerFields: Map[String, Seq[String]] = response.headerFields
    val rawCookies: Map[String, String] = response.rawCookies
    val html: String = response.asString // or response.textBody
    val bin: Array[Byte] = response.asBytes // or response.body

    // If you need to configure HTTP requests (e.g. adding some headers), use Request directly.
    val request = Request("http://example.com").header("Authorization", "OAuth realm: ...")
    val response2 = HTTP.get(request)
  }

  def curly_post_example:Unit = {
    //val response = HTTP.post("http://example.com/users", "aa=bb&ccc=123")

    val response = HTTP.post("http://example.com/users", Map("aaa" -> "bb", "ccc" -> 123))

    val response2: Response = HTTP.post("http://example.com/users",
      FormData(name = "name", text = TextInput("Andy", "UTF-8")),
      FormData(name = "profile_image", file = FileInput(new java.io.File("./myface.jpg"), "image/jpeg")))
      // or FormData(name = "bin", bytes = Array[Byte](1,2,3))
  }
}
