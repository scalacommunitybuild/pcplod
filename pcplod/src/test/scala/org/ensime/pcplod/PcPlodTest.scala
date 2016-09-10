// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pcplod

import org.scalatest._
import Matchers._

class PcPlodTest extends FlatSpec {

  "Mr Plod" should "typecheck a compilable valid noddy file" in withMrPlod("/com/acme/foo.scala") { mr =>
    mr.symbolAtPoint('foo) shouldBe Some("com.acme.Foo")
    mr.typeAtPoint('foo) shouldBe Some("com.acme.Foo.type")

    mr.symbolAtPoint('bar) shouldBe Some("com.acme.Foo.bar")
    mr.typeAtPoint('bar) shouldBe Some("Int")

    mr.symbolAtPoint('a) shouldBe Some("com.acme.Foo.a")
    mr.typeAtPoint('a) shouldBe Some("Int")

    mr.messages shouldBe empty
  }

  "Mr Plod" should "typecheck an uncompilable valid noddy file" in withMrPlod("/com/acme/foo_bad.scala") { mr =>
    mr.typeAtPoint('foo) shouldBe Some("com.acme.Foo.type")

    mr.typeAtPoint('input_a) shouldBe Some("<error>")
    println(mr.messages)

    import org.ensime.pcplod.PcMessageSeverity._
    val expected = List(
      PcMessage("/com/acme/foo_bad.scala", Error, "';' expected but '=' found."),
      PcMessage("/com/acme/foo_bad.scala", Error, "not found: value bar"),
      PcMessage("/com/acme/foo_bad.scala", Error, "not found: value a")
    )

    mr.messages shouldBe expected
  }

  "Noddy parser" should "support noddy syntax" in {
    val raw = "def ba@bar@r(a@a@: Int): Int = 2"
    val clean = "def bar(a: Int): Int = 2"

    PcPlod.parseNoddy(raw) shouldBe ((clean, Map("bar" -> 5, "a" -> 8)))
  }
}
