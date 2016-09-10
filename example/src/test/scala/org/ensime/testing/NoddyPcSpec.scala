// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0

// intentionally not in org.ensime.noddy for testing
package org.ensime.pctesting

import org.scalatest._
import org.scalatest.Matchers._

import org.ensime.pcplod._

class NoddyPcSpec extends FlatSpec {
  "@noddy" should "handle definitions of @noddy" in withMrPlod("/classes.scala") { mr: MrPlod =>
    mr.messages shouldBe Nil

    mr.symbolAtPoint('me) shouldBe Some("org.ensime.pctesting.Me")
    mr.typeAtPoint('me) shouldBe Some("org.ensime.pctesting.Me")

    mr.symbolAtPoint('myself) shouldBe Some("org.ensime.pctesting.Myself")
    mr.typeAtPoint('myself) shouldBe Some("org.ensime.pctesting.Myself")

    mr.symbolAtPoint('foo) shouldBe Some("org.ensime.pctesting.Myself.foo")
    mr.typeAtPoint('foo) shouldBe Some("String")
  }
}
