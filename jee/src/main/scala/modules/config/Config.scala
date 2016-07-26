package modules.config

trait Config {
  def webAppPort:Int = 52321
  def webAppDirs:Array[String] = Array("src/main/webapp", "../htdocs")
  def additionalScalatraBootstrapClasses:Array[Class[_ <: org.scalatra.LifeCycle]] = Array(
    classOf[modules.h2.ScalatraBootstrap],
    classOf[modules.scalikejdbc.ScalatraBootstrap],
    classOf[modules.cms.ScalatraBootstrap]
  )
  def scalikeJDBCLogLevel = 'DEBUG

  def defaultDatabaseURL:String = modules.h2.getMySQLCompatibleH2InMemoryDatabaseURL("playground")
  def defaultDatabaseMigrationLocations:Option[String] = Some("db/playground/migration")
  def defaultDatabaseUser:String = "sa"
  def defaultDatabasePassword:String = ""
}

object DefaultConfig extends Config {
}
