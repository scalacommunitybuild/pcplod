// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

object PCPlodUtil {
  /** Calculate the position in a file given line, col indexes */
  def calcPosForLineCol(file: String, line: Int, col: Int): Int = {
    if (line == 0) col
    else {
      val lines = file.split('\n').toIndexedSeq
      if (line > lines.size)
        file.length
      else {
        lines.take(line).map(_.length + 1).sum + col
      }
    }
  }
}
