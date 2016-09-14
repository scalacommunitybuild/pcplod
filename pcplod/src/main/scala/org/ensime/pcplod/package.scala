// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime

import java.io.File

package object pcplod {

  def withPcPlod[T](
    classpath: List[File] = Nil,
    options: List[String] = Nil
  )(f: PcPlod => T): T =
    f(new PcPlod(classpath, options))

  def withPcPlod[T](f: PcPlod => T): T = withPcPlod()(f)

  def withMrPlod[T](res: String)(f: MrPlod => T): T = f(MrPlod(res))

  implicit def posToPoint(pos: Int): Point = PositionPoint(pos)
  implicit def lineColToPoint(lc: (Int, Int)): Point = LineColumnPoint(lc._1, lc._2)
  implicit def noddyPoint(name: Symbol): Point = NoddyPoint(name)

}
