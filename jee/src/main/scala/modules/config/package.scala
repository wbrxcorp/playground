package modules

package object config {
  var currentConfig:Config = DefaultConfig

  def loadConfig(profile:String="default"):Unit = {
    this.synchronized {
      currentConfig =
        modules.reflect.getObjectByFullName("profiles.%s.Config".format(profile)).map(_.asInstanceOf[Config])
          .getOrElse(DefaultConfig)
    }
  }

  def get:Config = this.synchronized { currentConfig }
}
