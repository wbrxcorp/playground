package modules

package object scalikejdbc {
  import _root_.scalikejdbc._
  val config = modules.config.get
  implicit val session = AutoSession

  _root_.scalikejdbc.GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = config.scalikeJDBCLogLevel
  )

  def initDefaultDatabase():Unit = {
    ConnectionPool.singleton(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
  }
}
