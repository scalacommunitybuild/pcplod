// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

import org.scalatest._
import Matchers._

class PcPlodUtilTest extends FlatSpec {

  "PCPlodUtil" should "correctly calculate file offsets" in {
    PCPlodUtil.calcPosForLineCol("abc\nabc\nabc",0,0) shouldBe 0
    PCPlodUtil.calcPosForLineCol("abc\nabc\nabc",0,1) shouldBe 1
    PCPlodUtil.calcPosForLineCol("abc\nabc\nabc",2,1) shouldBe 9

    // EOF
    PCPlodUtil.calcPosForLineCol("abc\nabc\nabc",50,1) shouldBe 11
  }
}
