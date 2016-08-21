// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0

// intentionally not in org.ensime.noddy for testing
package org.ensime.testing

import scala.concurrent.Future
import org.scalatest._
import scala.annotation.StaticAnnotation
//import org.ensime.noddy._

class noddy(foo: Boolean, bar: Boolean, baz: Int) extends StaticAnnotation

@noddy
class Me

@noddy
class Myself(val foo: String, val bar: Long)

class Irene

@noddy
trait Mallgan

@noddy
object MyObj {
  def apply(foo: String, bar: Long): Me = null
}

@noddy
class Foo(foo: String, bar: Long) {
  val baz: String = foo // shouldn't be in constructor
}
object Foo {
  def ignore(foo: String, bar: Long): Foo = new Foo(foo, bar)
}

@noddy
class Baz[T](val fred: T)

@noddy
class Mine(val foo: String = "foo", val bar: Long = 13)

@noddy class Covariant[+I](item: I)
@noddy class Contravariant[-I](item: I)

@noddy
class LoggingFutures(a: String, b: Long) {
  def exposed = log
}
@noddy
object LoggingFutures {
  def exposed = log

  // FIXME: these need to be implemented by the plugin
  def a: Future[String] = null
  def b: Future[Long] = null
}

class NoddySpec extends FlatSpec with Matchers {
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
