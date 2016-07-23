package profiles.cms

object ImportModule extends profiles.ImportModule {
  def modules:Seq[String] = Seq("common","cms","webapp", "poi", "unirest", "opencsv", "hash")
}
