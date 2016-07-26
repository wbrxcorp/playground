package modules.scalikejdbc

class ScalatraBootstrap extends org.scalatra.LifeCycle {

  override def init(context: javax.servlet.ServletContext) {
    val config = modules.config.get

    scalikejdbc.GlobalSettings.loggingSQLAndTime = scalikejdbc.LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      logLevel = config.scalikeJDBCLogLevel
    )

    scalikejdbc.ConnectionPool.singleton(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
  }
}
