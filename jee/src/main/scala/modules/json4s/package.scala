package modules

import org.json4s.JValue

package object json4s {
  def loadJSON(in:java.io.InputStream):JValue = {
    org.json4s.jackson.JsonMethods.parse(in)
  }

  def loadJSON(filename:String):JValue = {
    modules.common.using(new java.io.FileInputStream(filename))(loadJSON(_))
  }

  def prettyPrint(value:JValue):Unit = {
    println(org.json4s.jackson.JsonMethods.pretty(value))
  }
}
