// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

import java.io.InputStream

import scala.annotation.tailrec
import scala.reflect.internal.util.BatchSourceFile

object PcPlod {
  def apply(): PcPlod = {
    //    -Dpcplod.pluginjar=/workspace/scala-compiler-plugin/plugins/target/scala-2.11/plugins_2.11-1.0.0-SNAPSHOT.jar

    val pluginJarURL = Option(System.getProperty("pcplod.pluginjar"))
    new PcPlod(pluginJarURL)
  }

  def apply(optPluginJar: Option[String]): PcPlod = {
    new PcPlod(optPluginJar)
  }
}

class PcPlod(optPluginJar: Option[String]) {

  case class FileInfo(path: String, contents: String, tokenLocations: Map[String, Int], f: BatchSourceFile)

  private var files: Map[String, FileInfo] = Map.empty
  val (pc, reporter) = PoshPresentationCompiler.create(optPluginJar)

  /**
   * Load a Scala file into the PC - the file is a resource location
   * N.b. the file contains tags (surrounded by @, to denote interesting locations in the code, these are stripped out
   * here.
   *
   * @param res A resource file containing the contents
   */
  def loadScala(res: String): Unit = {
    val stream: InputStream = getClass.getResourceAsStream(res)
    val rawInputStream = scala.io.Source.fromInputStream(stream)

    val rawContents = try {
      rawInputStream.getLines.mkString("\n")
    } finally {
      rawInputStream.close()
    }
    val (contents, symbols) = parseFile(rawContents)
    val f = pc.loadFile(res, contents)
    val fileInfo = FileInfo(res, contents, symbols, f)
    files += res -> fileInfo
  }

  def compilerWarnings: List[PcMessage] = {
    import reporter.{INFO, WARNING, ERROR}

    reporter.infos.map { info =>
      val severity = info.severity match {
        case INFO =>
          PcMessageSeverity.Info
        case WARNING =>
          PcMessageSeverity.Warning
        case ERROR =>
          PcMessageSeverity.Error
      }
      PcMessage(info.pos.source.file.toString, severity, info.msg)
    }(collection.breakOut)
  }

  val TokenRegex = "(?s)^([^@]*)@([^@]+)@(.*)$".r

  def parseFile(rawContents: String): (String, Map[String, Int]) = {
    @tailrec
    def extractSymbols(contents: String, symbols: Map[String, Int]): (String, Map[String, Int]) = {
      contents match {
        case TokenRegex(prequel, tag, sequel) =>
          extractSymbols(prequel + sequel, symbols + (tag -> prequel.length))
        case _ =>
          (contents, symbols)
      }
    }
    extractSymbols(rawContents, Map.empty)
  }

  def unloadScala(res: String): Unit = {
    pc.unloadFile(res)
    files -= res
  }

  def symbolAtPoint(res: String, p: Point): Option[String] = {
    // retrieve and use the FileInfo for the loaded file.
    files.get(res) match {
      case Some(fi) =>
        val idx: Int = p match {
          case NoddyPoint(symbol) =>
            val rawName = symbol.name
            fi.tokenLocations.get(rawName) match {
              case Some(filePos) =>
                filePos
              case None =>
                throw new IllegalArgumentException(s"Token $symbol not found in $res")
            }
          case PositionPoint(filePos) =>
            filePos
          case LineColumnPoint(line, col) =>
            ???
        }
        pc.askSymbolInfoAt(fi.f, idx)
      case None =>
        throw new IllegalArgumentException(s"res $res not loaded in PC")
    }
  }

  def typeAtPoint(res: String, p: Point): Option[String] = {
    // retrieve and use the FileInfo for the loaded file.
    files.get(res) match {
      case Some(fi) =>
        val idx: Int = p match {
          case NoddyPoint(symbol) =>
            val rawName = symbol.name
            fi.tokenLocations.get(rawName) match {
              case Some(filePos) =>
                filePos
              case None =>
                throw new IllegalArgumentException(s"Token $symbol not found in $res")
            }
          case PositionPoint(filePos) =>
            filePos
          case LineColumnPoint(line, col) =>
            ???
        }
        pc.askTypeAt(fi.f, idx)
      case None =>
        throw new IllegalArgumentException(s"res $res not loaded in PC")
    }
  }

  def messages: List[PcMessage] = {
    compilerWarnings
  }

}

object MrPlod {
  def apply(res: String): MrPlod = {
    new MrPlod(res, PcPlod())
  }
}

class MrPlod(
  val res: String,
  val pc: PcPlod
) {
  pc.loadScala(res)

  def symbolAtPoint(p: Point): Option[String] = pc.symbolAtPoint(res, p)

  def typeAtPoint(p: Point): Option[String] = pc.typeAtPoint(res, p)

  def messages: List[PcMessage] = pc.messages
}
