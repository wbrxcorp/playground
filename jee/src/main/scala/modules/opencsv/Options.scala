package modules.opencsv

import com.opencsv.{CSVParser,CSVReader,CSVWriter}

case class Options(
  separator:Char = CSVParser.DEFAULT_SEPARATOR,
  quotechar:Char= CSVParser.DEFAULT_QUOTE_CHARACTER,
  strictQuotes:Boolean = CSVParser.DEFAULT_STRICT_QUOTES,
  escape:Char = CSVParser.DEFAULT_ESCAPE_CHARACTER,
  line:Int = CSVReader.DEFAULT_SKIP_LINES,
  ignoreLeadingWhiteSpace:Boolean = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
  lineEnd:String = CSVWriter.DEFAULT_LINE_END,
  charset:String = "UTF-8"
)
