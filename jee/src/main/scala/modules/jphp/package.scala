package modules

import php.runtime.env.{CompileScope,ConcurrentEnvironment,Context,CallStackItem,TraceInfo}

package object jphp {
  def execphp(inputStream:java.io.InputStream,file:String):Unit = {
    val compileScope = new CompileScope
    val environment = new ConcurrentEnvironment(compileScope, System.out)
    val context = new Context(inputStream, file, environment.getDefaultCharset)
    val compiler = new org.develnext.jphp.core.compiler.jvm.JvmCompiler(environment, context);
    val moduleEntity = compiler.compile(false);

    compileScope.loadModule(moduleEntity)
    compileScope.addUserModule(moduleEntity)
    environment.registerModule(moduleEntity)

    environment.pushCall(new CallStackItem(new TraceInfo(moduleEntity.getName, -1, -1)));
    try {
      moduleEntity.includeNoThrow(environment)
    }
    finally {
      environment.popCall
      compileScope.triggerProgramShutdown(environment)
      environment.doFinal
    }
  }

  // modules.jphp.execphpfile("hoge.php")
  def execphpfile(file:String):Unit = {
    val inputStream = new java.io.FileInputStream(file)
    execphp(inputStream, file)
  }

  // modules.jphp.execphp("<?php echo 'hoge'?>")
  def execphp(script:String):Unit = {
    execphp(new java.io.ByteArrayInputStream(script.getBytes("UTF-8")), "script")
  }
}
