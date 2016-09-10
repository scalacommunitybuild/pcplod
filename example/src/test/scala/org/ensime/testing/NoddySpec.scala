// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0

// intentionally not in org.ensime.noddy for testing
package org.ensime.testing

import scala.concurrent.Future
import org.scalatest._
import org.scalatest.Matchers._

class NoddySpec extends FlatSpec {
  "@noddy" should "generate companion's apply with no parameters" in {
    { Me(): Me } shouldBe null
  }

  it should "create a companion for Mallgan" in {
    Mallgan shouldBe a[Mallgan.type]
  }

  it should "generate companion apply with parameters" in {
    { Myself("foo", 23L): Myself } shouldBe null
  }

  it should "update Foo's companion" in {
    Foo.ignore("foo", 13L) shouldBe a[Foo]

    { Foo("foo", 13L): Foo } shouldBe null
  }

  it should "generate companion apply with named / default parameters" in {
    { Mine("foo"): Mine } shouldBe null

    { Mine(foo = "foo"): Mine } shouldBe null

    { Mine(bar = 10): Mine } shouldBe null
  }

  it should "not create anything not @noddy" in {
    "Irene" shouldNot compile
  }

  it should "handle typed classes" in {
    { Baz("hello"): Baz[String] } shouldBe null
  }

  it should "handle covariant types" in {
    { Covariant(""): Covariant[String] } shouldBe null
  }

  it should "handle contravariant types" in {
    { Contravariant(""): Contravariant[String] } shouldBe null
  }

  it should "generate a log method on the instance" in {
    { new LoggingFutures("hello", 0).exposed } shouldBe null
  }

  it should "generate a log method on the companion" in {
    { LoggingFutures.exposed } shouldBe null
  }

  it should "generate future methods on the companion" in {
    { LoggingFutures.a: Future[String] } shouldBe null

    { LoggingFutures.b: Future[Long] } shouldBe null
  }

}
