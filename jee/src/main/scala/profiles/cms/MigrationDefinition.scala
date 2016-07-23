package profiles.cms

object MigrationDefinition extends modules.flyway.MigrationDefinition {
  def getLocations:String = "db/cms/migration"
}
