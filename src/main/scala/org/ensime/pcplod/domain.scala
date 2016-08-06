// Copyright: 2016 https://github.com/ensime/pcplod/graphs
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

sealed trait Point
case class PositionPoint(p: Int) extends Point
case class LineColumnPoint(line: Int, column: Int) extends Point
case class NoddyPoint(name: Symbol) extends Point

case class PcMessage()
