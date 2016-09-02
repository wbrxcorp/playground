package modules

import com.typesafe.config.ConfigFactory

package object config extends com.typesafe.scalalogging.slf4j.LazyLogging {
  def projectName:String = buildinfo.BuildInfo.name

  val defaultConfigs = Map("default"->DefaultDefaultConfig, "standalone"->DefaultStandaloneConfig)
  var currentConfig:Config = DefaultStandaloneConfig // loadConfigを一度も呼び出さない場合のデフォルト
  val staticConfig = ConfigFactory.load

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
