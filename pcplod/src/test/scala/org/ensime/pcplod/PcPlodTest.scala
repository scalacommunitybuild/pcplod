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

    mr.messages shouldBe 'empty
  }

  "Mr Plod" should "typecheck an uncompilable valid noddy file" in withMrPlod("/com/acme/foo_bad.scala") { mr =>
    mr.typeAtPoint('foo) shouldBe Some("com.acme.Foo.type")

    mr.typeAtPoint('input_a) shouldBe Some("<error>")
    println(mr.messages)
    mr.messages should contain only ()
  }

}
