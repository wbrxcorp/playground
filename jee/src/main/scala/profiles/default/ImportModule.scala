package profiles.default

object ImportModule extends profiles.ImportModule {
  def modules:Seq[String] = Seq("common","database","webapp", "poi", "unirest")
}
