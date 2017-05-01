package modules

package object flexmark {
  val yamlFrontMatterExtension = com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension.create()

  def testFlexMark:Unit = {
    val options = new com.vladsch.flexmark.util.options.MutableDataSet()
    val extensions = new java.util.HashSet[com.vladsch.flexmark.Extension]()
    extensions.add(yamlFrontMatterExtension)
    options.setFrom(com.vladsch.flexmark.parser.ParserEmulationProfile.GITHUB_DOC)
    val parser = com.vladsch.flexmark.parser.Parser.builder(options).extensions(extensions).build
    val document = parser.parse("---\ntitle: 俺がFrontMatterだ\ndate:2017-05-01\nfoo: [1,2,3]\n---\n\n# あぼー")
    val html = com.vladsch.flexmark.html.HtmlRenderer.builder(options).build.render(document)
    println(html)

    val visitor = new com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor()
    visitor.visit(document)
    pprint.pprintln(visitor.getData)
  }
}
