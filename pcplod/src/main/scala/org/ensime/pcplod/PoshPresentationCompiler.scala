// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

import scala.reflect.internal.util.{BatchSourceFile, OffsetPosition}
import scala.tools.nsc.Settings
import scala.tools.nsc.interactive.Global
import scala.tools.nsc.reporters.{Reporter, StoreReporter}

private[pcplod] object PoshPresentationCompiler {
  def create(pluginJarPathOpt: Option[String]): (PoshPresentationCompiler, StoreReporter) = {

    val settings = new Settings(s => println(s"PC: $s"))
    settings.YpresentationDebug.value = true
    settings.YpresentationVerbose.value = true
    settings.verbose.value = true
    settings.usejavacp.value = true
    pluginJarPathOpt match {
      case Some(jarPath) =>
        settings.processArguments(List(s"-Xplugin:$jarPath"), processAll = false)
      case None =>
    }

    val reporter = new StoreReporter()

    (new PoshPresentationCompiler(settings, reporter), reporter)
  }
}

private[pcplod] class PoshPresentationCompiler(
  override val settings: Settings,
  val richReporter: Reporter
) extends Global(settings, richReporter) with PoshCompilerControl {

  def loadFile(path: String, contents: String): BatchSourceFile = {
    val f = new BatchSourceFile(path, contents)
    askLoadedTyped(f)
    f
  }

  def loadFile(path: String): Unit = {
    val f = createSourceFile(path)
    askLoadedTyped(f)
  }

  def unloadFile(path: String): Unit = {
    val f = createSourceFile(path)
    askRemoveFile(f)
  }

  /*
  * The following functions wrap up operations that interact with
  * the presentation compiler. The wrapping just helps with the
  * create response / compute / get result pattern.
  *
  * These units of work should return `Future[T]`.
  */
  def wrap[A](compute: Response[A] => Unit, handle: Throwable => A): A = {
    val result = new Response[A]
    compute(result)
    result.get.fold(o => o, handle)
  }

  protected def symbolAt(pos: Position): Option[Symbol] = {
    val tree = wrapTypedTreeAt(pos)
    // This code taken mostly verbatim from Scala IDE sources.
    val wannabes =
      tree match {
        case Import(expr, selectors) =>
          if (expr.pos.includes(pos)) {
            @annotation.tailrec
            def locate(p: Position, inExpr: Tree): Symbol = inExpr match {
              case Select(qualifier, name) =>
                if (qualifier.pos.includes(p)) locate(p, qualifier)
                else inExpr.symbol
              case tree => tree.symbol
            }
            List(locate(pos, expr))
          } else {
            selectors.filter(_.namePos <= pos.point).sortBy(_.namePos).lastOption map { sel =>
              val tpe = stabilizedType(expr)
              List(tpe.member(sel.name), tpe.member(sel.name.toTypeName))
            } getOrElse Nil
          }
        case Annotated(atp, _) =>
          List(atp.symbol)
        case st: SymTree if st.symbol ne null =>
          println("DEBUG: using symbol of " + tree.getClass + " tree")
          List(tree.symbol)
        case lit: Literal =>
          List(lit.tpe.typeSymbol)
        case _ =>
          println("WARN symbolAt for " + tree.getClass + ": " + tree)
          Nil
      }
    wannabes.find(_.exists)
  }

  def wrapTypedTreeAt(position: Position): Tree =
    wrap[Tree](r => AskTypeAtItem(position, r).apply(), t => throw t)

  def askSymbolInfoAt(f: BatchSourceFile, idx: Int): Option[String] = {
    val pos = new OffsetPosition(f, idx)
    askOption(symbolAt(pos).map {
      (x: Symbol) =>
        x.fullName
    }).flatten
  }

  def askTypeAt(f: BatchSourceFile, idx: Int): Option[String] = {
    val pos = new OffsetPosition(f, idx)
    askOption(typeAt(pos).map {
      (x: Type) =>
        x.safeToString
    }).flatten
  }
}
