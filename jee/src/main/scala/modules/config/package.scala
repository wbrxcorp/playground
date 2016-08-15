package modules

package object config extends com.typesafe.scalalogging.slf4j.LazyLogging {
  val defaultConfigs = Map("default"->DefaultDefaultConfig, "standalone"->DefaultStandaloneConfig)
  var currentConfig:Config = if (System.getProperty("sbt.version") == null) DefaultDefaultConfig else DefaultStandaloneConfig // loadConfigを一度も呼び出さない場合のデフォルト

  def loadConfig(profile:String="default"):Unit = {
    this.synchronized {
      currentConfig =
        modules.reflect.getObjectByFullName("profiles.%s.Config".format(profile)).map(_.asInstanceOf[Config])
          .getOrElse(defaultConfigs(profile))
      logger.info("currentConfig: %s".format(currentConfig.toString))
    }
  }

  def get:Config = this.synchronized { currentConfig }
}
