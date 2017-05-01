package modules

import collection.JavaConverters._

package object jinjava {
  def testJinjava:Unit = {
    val jinjava = new com.hubspot.jinjava.Jinjava()
    jinjava.setResourceLocator(new com.hubspot.jinjava.loader.ClasspathResourceLocator())
    // new com.hubspot.jinjava.loader.FileLocator(new java.io.File("your/template/path"))
    val template = "hello.html"
    println(jinjava.render(s"""{% include "jinjava-templates/${template}" %}""", mapAsJavaMap(Map("name"->"shimariso"))))
  }
}
