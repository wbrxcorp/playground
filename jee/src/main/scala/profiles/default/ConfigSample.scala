package profiles.default

// default プロファイルは warでアプリケーションサーバにデプロイされた時を想定

// create a copy of this as "Config" object

object ConfigSample extends modules.config.Config {
  override def webAppPort = 12345
  override def webAppDirs:Array[String] = Array("src/main/webapp","../htdocs","../htdocs-nodeploy")
  //override def additionalScalatraBootstrapClasses:Array[Class[_ <: LifeCycle]] = Array(classOf[modules.myapp.ScalatraBootstrap])
}
