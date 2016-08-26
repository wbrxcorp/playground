package modules

import collection.JavaConversions._
import php.runtime.env.{CompileScope,ConcurrentEnvironment,Context,CallStackItem,TraceInfo}
import php.runtime.memory.{LongMemory,ArrayMemory}
import php.runtime.reflection.ModuleEntity

package object jphp {
  val random = new scala.util.Random

  def compilephp(inputStream:java.io.InputStream,file:String):ModuleEntity = {
    val compileScope = new CompileScope
    val environment = new ConcurrentEnvironment(compileScope, System.out)
    val context = new Context(inputStream, file, environment.getDefaultCharset)
    val compiler = new org.develnext.jphp.core.compiler.jvm.JvmCompiler(environment, context);
    compiler.compile(false);
  }

  def execphp(moduleEntity:ModuleEntity):Unit = {
    val compileScope = new CompileScope
    val environment = new ConcurrentEnvironment(compileScope, System.out)
    compileScope.loadModule(moduleEntity)
    compileScope.addUserModule(moduleEntity)
    environment.registerModule(moduleEntity)
    environment.getGlobals.put("_SESSION", ArrayMemory.ofMap(Map("userId"->LongMemory.valueOf(random.nextInt(100)))))
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

  def execphp(inputStream:java.io.InputStream,file:String):Unit = {
    val compileScope = new CompileScope
    val environment = new ConcurrentEnvironment(compileScope, System.out)
    val context = new Context(inputStream, file, environment.getDefaultCharset)
    val compiler = new org.develnext.jphp.core.compiler.jvm.JvmCompiler(environment, context);
    val moduleEntity = compiler.compile(false);

    compileScope.loadModule(moduleEntity)
    compileScope.addUserModule(moduleEntity)
    environment.registerModule(moduleEntity)
    environment.getGlobals.put("_SESSION", ArrayMemory.ofMap(Map("userId"->LongMemory.valueOf(2))))

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
    modules.common.using(new java.io.FileInputStream(file)) { inputStream =>
      execphp(inputStream, file)
    }
  }

  // modules.jphp.execphp("<?php echo 'hoge'?>")
  def execphp(script:String):Unit = {
    execphp(new java.io.ByteArrayInputStream(script.getBytes("UTF-8")), "script")
  }

  def compilephp(script:String):ModuleEntity = {
    compilephp(new java.io.ByteArrayInputStream(script.getBytes("UTF-8")), "script")
  }
}
