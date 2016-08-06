// Copyright: 2016 https://github.com/ensime/pcplod/graphs
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

class PcPlod {

  def loadScala(res: String): Unit = ???
  def unloadScala(res: String): Unit = ???

  def symbolAtPoint(res: String, p: Point): Option[String] = ???
  def typeAtPoint(res: String, p: Point): Option[String] = ???
  def messages: List[PcMessage] = ???

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
