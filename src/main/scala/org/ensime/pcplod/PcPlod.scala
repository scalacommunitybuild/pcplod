// Copyright: 2016 https://github.com/ensime/pcplod/graphs
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

import java.io.InputStream

import org.ensime.pcplod.internal.RichishPresentationCompiler

import scala.annotation.tailrec
import scala.reflect.internal.util.{BatchSourceFile, OffsetPosition}

object PcPlod {
  def apply(classpath: String): PcPlod = {
    new PcPlod(classpath, "/Users/rorygraves/.coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.11.8/scala-library-2.11.8.jar")
  }

  def apply(): PcPlod = {
    apply("")
  }

//  def main(args: Array[String]): Unit = {
//
//    val text = """// Copyright: 2016 https://github.com/ensime/pcplod/graphs
//                 |// License: http://www.apache.org/licenses/LICENSE-2.0
//                 |package com.acme
//                 |
//                 |object F@foo@oo {
//                 |  def bar(a@input_a@: String): Int = ???
//                 |}""".stripMargin
//
//    val text2 =
//      """fadf@foo@blah\nabc""".stripMargin
//
//    @tailrec
//    def extractSymbols(contents: String, symbols: Map[String, Int]): (String, Map[String, Int]) = {
//      contents match {
//        case R(prequal, tag, sequal) =>
//          println("HERE")
//          extractSymbols(prequal + sequal, symbols + (tag -> prequal.size))
//        case _ =>
//          (contents, symbols)
//      }
//    }
//    println(extractSymbols(text, Map.empty))
//
//  }
}

class PcPlod(classpath: String, scalaLibrary: String) {

  case class FileInfo(path: String, contents: String, tokenLocations: Map[String, Int], f: BatchSourceFile)

  private var files: Map[String,FileInfo] = Map.empty
  val pc = RichishPresentationCompiler.create(scalaLibrary, classpath)

  /** Load a Scala file into the PC - the file is a resource location
    * N.b. the file contains tags (surrounded by @, to denote interesting locations in the code, these are stripped out
    * here.
    * @param res A resource file containing the contents
    */
  def loadScala(res: String): Unit = {
    val stream : InputStream = getClass.getResourceAsStream(res)
    val rawContents = scala.io.Source.fromInputStream( stream ).getLines.mkString("\n")
    val (contents, symbols) = parseFile(rawContents)
    val f = pc.loadFile(res, contents)
    println("SLEEP TO MAKE IT WORK! - NEEDS FIX")
    Thread.sleep(5000) // TODO We need to wait until the compiler is finished loading the file.
    val fileInfo = FileInfo(res, contents, symbols, f)
    files += res -> fileInfo
  }

  val TokenRegex = "(?s)^(.*)@([^@]+)@(.*)$".r


  def parseFile(rawContents: String): (String, Map[String, Int]) = {
    import scala.annotation.tailrec
    @tailrec
    def extractSymbols(contents: String, symbols: Map[String, Int]): (String, Map[String, Int]) = {
      contents match {
        case TokenRegex(prequal, tag, sequal) =>
          extractSymbols(prequal + sequal, symbols + (tag -> prequal.size))
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
            // TODO
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
            // TODO
            ???
        }
        pc.askTypeAt(fi.f, idx)
      case None =>
        throw new IllegalArgumentException(s"res $res not loaded in PC")
    }
  }

  def messages: List[PcMessage] = ???

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
