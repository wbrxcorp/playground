package modules

trait Module {
  def dependsOn():Seq[String] = Nil
  def init(factory:profiles.Factory,repl:scala.tools.nsc.interpreter.ILoop):Unit = {}
  def destroy():Unit = {}
}

trait Using {
  type Closable = { def close():Unit }
  def using[A <: Closable,B]( resource:A )( f:A => B ) = try(f(resource)) finally(resource.close)
}
