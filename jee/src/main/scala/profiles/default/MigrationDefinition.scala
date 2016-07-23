package profiles.default

object MigrationDefinition extends modules.flyway.MigrationDefinition {
  def getLocations:String = "db/migration"
}
