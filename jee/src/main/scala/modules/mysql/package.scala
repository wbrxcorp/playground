package modules

package object mysql {
  def getMySQLDatabaseURL(dbname:String,hostname:String="localhost",settings:ConnectionSettings=ConnectionSettings()):String = {
    "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&useCompression=true&autoReconnect=true&socketTimeout=10000".format(hostname, dbname) + (settings.serverTimezone match {
      case Some(timezone) => "&useLegacyDatetimeCode=false&serverTimezone=%s".format(timezone)
      case None => ""
    })
  }
}
