package modules.scalikejdbc

class ScalatraBootstrap extends org.scalatra.LifeCycle {

  override def init(context: javax.servlet.ServletContext) {
    val config = modules.config.get

    scalikejdbc.GlobalSettings.loggingSQLAndTime = scalikejdbc.LoggingSQLAndTimeSettings(
      enabled = true,
      singleLineMode = true,
      logLevel = config.scalikeJDBCLogLevel
    )

    val flyway = new org.flywaydb.core.Flyway()
    flyway.setDataSource(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
    config.defaultDatabaseMigrationLocations.foreach(flyway.setLocations(_))
    flyway.migrate()

    scalikejdbc.ConnectionPool.singleton(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
  }
}
