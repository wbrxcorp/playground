package modules.cms

class ScalatraBootstrap extends org.scalatra.LifeCycle {


  override def init(context: javax.servlet.ServletContext) {
    val url = modules.h2.getMySQLCompatibleH2InMemoryDatabaseURL("cms")
    val flyway = new org.flywaydb.core.Flyway()
    flyway.setLocations("db/cms/migration")
    flyway.setDataSource(url, "sa", "")
    flyway.migrate()

    scalikejdbc.ConnectionPool.add("cms", url, "sa", "")

    context.mount(classOf[modules.cms.Api], "/cms/api.php/*")
  }
}
