// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0

// intentionally not in org.ensime.noddy for testing
package org.ensime.pctesting

import org.scalatest._
import org.scalatest.Matchers._

import org.ensime.pcplod._

class NoddyPcSpec extends FlatSpec {
  "@noddy" should "not generate errors" in withMrPlod("/classes.scala") { mr: MrPlod =>
    mr.symbolAtPoint('me) shouldBe "org.ensime.pctesting.Me"
    mr.typeAtPoint('me) shouldBe "org.ensime.pctesting.Me"

  }
}
