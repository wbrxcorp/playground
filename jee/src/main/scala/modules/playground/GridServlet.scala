package modules.playground

case class GridColumnDef(name:String,label:Option[String])

case class GridConfig (
  name:String,
  label:Option[String],
  previewColumns:Seq[GridColumnDef] = Nil,
  downloadColumns:Seq[GridColumnDef] = Nil
)

class GridServlet extends modules.scalatra.JsonSupport {
  import modules.common.using
  import modules.opencsv._

  def withResourceAsStream[T](name:String)(f:java.io.InputStream=>T):Option[T] = {
    using(this.getClass.getResourceAsStream(name))(Option(_).map(f(_)))
  }

  val gridDataSource = "/playground/dummy-personal-info.tsv"  // http://kazina.com/dummy/
  val gridConfigSource = "/playground/grid.json"  // プレビューとダウンロードのそれぞれ項目リストを定義したファイル
  val gridData = withResourceAsStream(gridDataSource)(withCSVReader(_, modules.opencsv.Options(separator='\t'))(modules.opencsv.toListOfMap(_))).get
  val gridConfig = withResourceAsStream(gridConfigSource)(json => (org.json4s.jackson.JsonMethods.parse(json) \\ "gridConfig").extract[Seq[GridConfig]]).get
  val gridConfigMap = gridConfig.map { gridConfig =>
    (gridConfig.name, gridConfig)
  }.toMap

  def getGridConfig(name:String):GridConfig = {
    gridConfigMap.get(name).getOrElse(halt(404, "Grid config '%s' not found".format(name)))
  }

  get("/") {
    contentType = formats("json")
    gridConfig
  }

  get("/:gridConfigName/preview") {
    val chosenGridConfig = getGridConfig(params("gridConfigName"))
    contentType = formats("json")
    Map(
      "header"->chosenGridConfig.previewColumns.map(_ match { case GridColumnDef(_, Some(label)) => label; case GridColumnDef(name,None) => name}),
      "data"->gridData.map { row =>
        chosenGridConfig.previewColumns.map(col => row.get(col.name).getOrElse(""))
      }
    )
  }

  get("/:gridConfigName/download.csv") {
    val gridConfigName = params("gridConfigName")
    val chosenGridConfig = getGridConfig(gridConfigName)
    contentType = "application/octet-stream"
    response.setHeader("Content-Disposition", "attachment; filename=download-%s.csv".format(gridConfigName))
    using(new com.opencsv.CSVWriter(new java.io.OutputStreamWriter(response.getOutputStream, "MS932"))) { writer =>
      writer.writeNext(chosenGridConfig.downloadColumns.map(_ match { case GridColumnDef(_, Some(label)) => label; case GridColumnDef(name,None) => name}).toArray)
      gridData.foreach { row =>
        writer.writeNext(chosenGridConfig.downloadColumns.map(col => row.get(col.name).getOrElse("")).toArray)
      }
    }
  }

}
