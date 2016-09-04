package modules

import scala.collection.JavaConversions._
import com.opencsv.{CSVWriter, CSVReader}
import modules.common.using

package object opencsv {
  def withCSVWriter[T](out:java.io.OutputStream, options:Options=Options())(f:CSVWriter=>T):T = {
    using (new java.io.OutputStreamWriter(out, options.charset)) { osw =>
      using (new CSVWriter(osw, options.separator, options.quotechar, options.escape, options.lineEnd)) { writer =>
        f(writer)
      }
    }
  }

  def withCSVWriterToFile[T](filename:String, options:Options=Options())(f:CSVWriter=>T):T = {
    using (new java.io.FileOutputStream(filename)) { out =>
      withCSVWriter(out, options)(f)
    }
  }

  def withCSVReader[T](in:java.io.InputStream, options:Options=Options())(f:CSVReader=>T):T = {
    using (new java.io.InputStreamReader(in, options.charset)) { isr =>
      using (new CSVReader(isr, options.separator, options.quotechar, options.escape, options.line, options.strictQuotes, options.ignoreLeadingWhiteSpace)) { reader =>
        f(reader)
      }
    }
  }

  def withCSVReaderFromFile[T](filename:String, options:Options=Options())(f:CSVReader=>T):T = {
    using (new java.io.FileInputStream(filename)) { in =>
      withCSVReader(in, options)(f)
    }
  }

  def toListOfMap(reader:CSVReader):Seq[Map[String,String]] = {
    val i = reader.iterator()
    if (!i.hasNext()) return Seq()
    val firstLine = i.next() // first line to header

    i.map { line =>
      line.zipWithIndex.map { case (col, index) =>
        (
          if (index < firstLine.length) firstLine(index) else "COLUMN%d".format(index),
          col
        )
      }.toMap
    }.toList
  }
}
