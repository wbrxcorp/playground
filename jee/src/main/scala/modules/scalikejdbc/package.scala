package modules

package object scalikejdbc {
  import _root_.scalikejdbc._
  val config = modules.config.get
  implicit val session = AutoSession

  _root_.scalikejdbc.GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = config.scalikeJDBCLogLevel
  )

  def initDefaultDatabase():Unit = {
    ConnectionPool.singleton(config.defaultDatabaseURL, config.defaultDatabaseUser, config.defaultDatabasePassword)
  }

  def p(query:_root_.scalikejdbc.SQL[Nothing,_root_.scalikejdbc.NoExtractor])(implicit session:_root_.scalikejdbc.DBSession):Unit = {
    val rows = query.toMap.list.apply
    val header = rows.headOption.map { firstRow =>
      firstRow.map(_._1)
    }.getOrElse(Seq("EMPTY RESULT"))

    val rowsArray = rows.map { row =>
      row.map(_._2.toString).toArray
    }.toArray

    println(com.jakewharton.fliptables.FlipTable.of(header.toArray,rowsArray))
  }
}
