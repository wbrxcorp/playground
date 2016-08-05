package modules

package object scalikejdbc {
  val config = modules.config.get

  _root_.scalikejdbc.GlobalSettings.loggingSQLAndTime = _root_.scalikejdbc.LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = config.scalikeJDBCLogLevel
  )

  def initDefaultDatabase():Unit = {
    _root_.scalikejdbc.ConnectionPool.singleton(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
  }
}
