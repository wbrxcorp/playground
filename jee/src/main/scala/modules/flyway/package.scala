package modules

package object flyway {
  def migrate(url:String,username:String,password:String,locations:String="db/migration"):Unit = {
    val flyway = new org.flywaydb.core.Flyway()
    flyway.setLocations(locations)
    flyway.setDataSource(url, username, password)
    flyway.migrate()
  }
}
