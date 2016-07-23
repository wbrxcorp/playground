package modules.opencsv

import com.opencsv.{CSVWriter, CSVReader}

object Module extends modules.Module with modules.Using {
  //override def init(factory:profiles.Factory,repl:scala.tools.nsc.interpreter.ILoop):Unit = {}

  def withCSVWriter[T](out:java.io.OutputStream, charset:String="UTF-8")(f:CSVWriter=>T):T = {
    using (new java.io.OutputStreamWriter(out, charset)) { osw =>
      using (new CSVWriter(osw)) { writer =>
        f(writer)
      }
    }
  }

  def withCSVWriterToFile[T](filename:String, charset:String="UTF-8")(f:CSVWriter=>T):T = {
    using (new java.io.FileOutputStream(filename)) { out =>
      withCSVWriter(out, charset)(f)
    }
  }

  def withCSVReader[T](in:java.io.InputStream, charset:String="UTF-8")(f:CSVReader=>T):T = {
    using (new java.io.InputStreamReader(in, charset)) { isr =>
      using (new CSVReader(isr)) { reader =>
        f(reader)
      }
    }
  }

  def withCSVReaderFromFile[T](filename:String, charset:String="UTF-8")(f:CSVReader=>T):T = {
    using (new java.io.FileInputStream(filename)) { in =>
      withCSVReader(in, charset)(f)
    }
  }
}
