package modules.playground

class ScalatraBootstrap extends org.scalatra.LifeCycle with com.typesafe.scalalogging.slf4j.LazyLogging {
  override def init(context: javax.servlet.ServletContext):Unit = {
    import modules.image._
    val image = dummyImage(512, 384)
    val id = putImage(ImageFormat.PNG.toContentType, image)
    logger.info("Test dummy image: /playground/blobtest/%s.png".format(id))
    context.mount(classOf[modules.blobstore.BlobServlet], "/playground/blob/*")
    context.mount(classOf[modules.image.ResizeImageServlet], "/playground/resizeimage/*")
    context.mount(classOf[modules.playground.GridServlet], "/playground/grid/*")
    context.mount(new modules.playground.FormServlet, "/playground/form/*") // you shouldn't use classOf[Servlet] style when you need to enable MultipartConfig
    context.mount(classOf[modules.playground.DelayServlet], "/playground/delay/*")
  }
}
