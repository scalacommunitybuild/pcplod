// Copyright: 2010 - 2016 Rory Graves, Sam Halliday
// License: http://www.apache.org/licenses/LICENSE-2.0
package org.ensime.pctesting

import org.ensime.annotation.noddy
import scala.concurrent.Future

@noddy
class M@me@e

@noddy
class My@myself@self(val fo@foo@o: String, val bar: Long)

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

  def a: Future[String] = null
  def b: Future[Long] = null
}

object Testing {
  val me = Me(): Me
}
