// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime

package object pcplod {

  def withPcPlod[T](classpath: String)(f: PcPlod => T): T = {
    f(PcPlod(classpath))
  }
  def withPcPlod[T](f: PcPlod => T): T = {
    f(PcPlod())
  }

  def withMrPlod[T](res: String)(f: MrPlod => T): T = {
    f(MrPlod(res))
  }

  implicit def posToPoint(pos: Int): Point = PositionPoint(pos)
  implicit def lineColToPoint(lc: (Int, Int)): Point = LineColumnPoint(lc._1, lc._2)
  implicit def noddyPoint(name: Symbol): Point = NoddyPoint(name)

}
