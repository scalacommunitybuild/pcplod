// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

sealed trait Point
case class PositionPoint(p: Int) extends Point
case class LineColumnPoint(line: Int, column: Int) extends Point
case class NoddyPoint(name: Symbol) extends Point


sealed abstract class PcMessageSeverity

object PcMessageSeverity {
  case object Info extends PcMessageSeverity
  case object Warning extends PcMessageSeverity
  case object Error extends PcMessageSeverity
}

case class PcMessage(file: String, severity: PcMessageSeverity, message: String)
