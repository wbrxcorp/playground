package profiles

class Factory(profile:String) {
  val defaultPackage = "profiles.default"
  val profilePackage = "profiles.%s".format(profile)

  def getObjectByName(name:String):Any = {
    scala.util.Try(Factory.getObjectByFullName("%s.%s".format(profilePackage, name))).getOrElse(Factory.getObjectByFullName("%s.%s".format(defaultPackage, name)))
  }

  def getClassByName(name:String):Class[_ <: Any] = {
    scala.util.Try(Class.forName("%s.%s".format(profilePackage, name))).getOrElse(Class.forName("%s.%s".format(defaultPackage, name)))
  }
}

object Factory {
  def getObjectByFullName(name:String):Any = {
    val runtimeMirror = scala.reflect.runtime.universe.runtimeMirror(getClass.getClassLoader)
    val module = runtimeMirror.staticModule(name)
    val obj = runtimeMirror.reflectModule(module)
    obj.instance
  }
}
