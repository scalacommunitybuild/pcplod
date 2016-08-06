// Copyright: 2016 https://github.com/ensime/pcplod/graphs
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime

package object pcplod {

  def withPcPlod[T](classpath: String)(pc: PcPlod => T): T = ???
  def withPcPlod[T](pc: PcPlod => T): T = ???

  def withMrPlod[T](res: String)(f: MrPlod => T): T = ???

  implicit def posToPoint(pos: Int): Point = PositionPoint(pos)
  implicit def lineColToPoint(lc: (Int, Int)): Point = LineColumnPoint(lc._1, lc._2)
  implicit def noddyPoint(name: Symbol): Point = NoddyPoint(name)

}
