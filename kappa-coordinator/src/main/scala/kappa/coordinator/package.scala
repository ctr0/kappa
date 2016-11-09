package kappa

import kappa.Directives._

import scala.util.Try

/**
  * Created by j0rd1 on 4/11/16.
  */
package object coordinator {

  /**
    * Created by j0rd1 on 3/11/16.
    */
  case class CoordinatorException(c: Class[_], path: String, error: String, cause: Throwable = null)
    extends Exception(error, cause) {

    override def getMessage: String = s"[${c.getSimpleName}: $path] -> ${super.getMessage}"
  }





  /*
    def success[R](f: ⇒ Directive[R]): SuccessDirective[R] = new SuccessDirective[R](f)

    class SuccessDirective[R](f: ⇒ Directive[R]) extends Directive[R] {

      private var failureFunction: Throwable ⇒ Directive[R] = _

      private[coordinator] def get: Directive[R] = f

      override def apply(): R = f.apply()

      override def success(f: Directive[R]): Directive[R] = ???

      override def success[T](f: (T) => Directive[R]): Directive[R] = ???

      def failure(f: Throwable ⇒ Directive[R]): Directive[R] = {
        failureFunction = f
        this
      }

      private[coordinator] def rollback(t: Throwable): Unit = {
        if (failureFunction != null)
          failureFunction(t)
      }
    }

    trait Assertion[R] extends Directive[R]
    trait AssertLockedDirective[R] extends Directive[R]
    trait AssertNotLockedDirective[R] extends Directive[R]
  */
  /*
  def assertLocked[R](lock: Lock, paths: Path*)(f: ⇒ Directive[R]): AssertLockedDirective[R] = {

    f match {
      case a: AssertLockedDirective ⇒ null
      case a: AssertNotLockedDirective ⇒ null
      case s: SuccessDirective ⇒

    }

    null
  }

  private def chainAssertions(f: Directive[_]): Option[(Directive, Directive)] = {
    f match {
      case a: AssertLockedDirective ⇒ Some((a, null))
      case a: AssertNotLockedDirective ⇒ Some((a, null))
      case s: SuccessDirective ⇒
        val assertion = chainAssertions(s.get)
        if (assertion != null) {
          Some(assertion, )
        }
      case _ ⇒ null

    }
  }
*/
}
