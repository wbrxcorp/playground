package modules.flyway

class ScalatraBootstrap extends org.scalatra.LifeCycle {

  override def init(context: javax.servlet.ServletContext) {
    val config = modules.config.get

    val flyway = new org.flywaydb.core.Flyway()
    flyway.setDataSource(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
    config.defaultDatabaseMigrationLocations.foreach(flyway.setLocations(_))
    flyway.migrate()
  }
}
