package modules.config

import collection.JavaConversions._
import org.scalatra.LifeCycle

trait Config {
  def projectName:String = buildinfo.BuildInfo.name
  // Jetty設定
  def webAppPort:Int = staticConfig.getInt("modules.jetty.webAppPort") match {
    case 0 =>
      51234 + Integer.parseInt(modules.hash.sha1(projectName).substring(0,3), 16)// プロジェクト名のsha1sum文字列から3文字(4096種類)取ってポート番号にする
    case x => x
  }
  def webAppDirs:Array[String] = staticConfig.getStringList("modules.jetty.webAppDirs").toList.toArray

  // Scalatra設定
  def additionalScalatraBootstrapClasses:Array[Class[_ <: LifeCycle]] = {
    (
      Seq(
        classOf[modules.h2.ScalatraBootstrap],
        classOf[modules.scalikejdbc.ScalatraBootstrap],
        classOf[modules.flyway.ScalatraBootstrap],
        classOf[modules.highlight.ScalatraBootstrap]
      ) ++
      modules.reflect.getClassByFullName("modules.%s.ScalatraBootstrap".format(buildinfo.BuildInfo.name)).map(_.asInstanceOf[Class[LifeCycle]])
    ).toArray
  }

  // ScalikeJDBC設定
  def scalikeJDBCLogLevel = 'DEBUG

  // デフォルトデータベース設定. 環境にJNDIがなければJDBC URLでの接続にフォールバックする
  def defaultJNDIDataSourceName = "jdbc/%s".format(projectName)
  def defaultDatabaseURL:String = modules.h2.getMySQLCompatibleH2InMemoryDatabaseURL(projectName)
  def defaultDatabaseMigrationLocations:Option[String] = Some("db/%s/migration".format(projectName))
  def defaultDatabaseMigration:Boolean = staticConfig.getBoolean("modules.flyway.migrateDefaultDatabase")
  def defaultDatabaseUser:String = "sa"
  def defaultDatabasePassword:String = ""

  // ソースコードハイライトシステム設定
  def highlightRoot:String = ".."
}

// warでTomcatとかにデプロイされた時用のデフォルト設定
trait DefaultDefaultConfig extends Config {
  // データベース(MySQL)
  override def defaultDatabaseURL:String = modules.mysql.getMySQLDatabaseURL(projectName, "localhost")
  override def defaultDatabaseUser:String = projectName
  override def defaultDatabaseMigration:Boolean = staticConfig.getBoolean("default.modules.flyway.migrateDefaultDatabase")

  // ソースコードハイライトシステム設定
  override def highlightRoot:String = "/home/wbrxcorp/playground"
}

// sbt runされた時用のデフォルト設定
trait DefaultStandaloneConfig extends Config {
}

object DefaultDefaultConfig extends DefaultDefaultConfig {
}

object DefaultStandaloneConfig extends DefaultStandaloneConfig {
}
