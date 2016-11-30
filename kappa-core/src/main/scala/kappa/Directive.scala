package kappa

import java.util.function.{Function => JavaFunction}

import scala.collection.mutable
import scala.util.control.NonFatal


object Directive {
  def Directive0(name: String)(f: KappaSession => Unit) = new Directive0(name, f)
  //def Directive0(name: String)(f: JavaFunction[KappaSession, Void]) = new Directive0(name, f)
  def Directive1[T](name: String)(f: KappaSession => T) = new Directive1[T](name, f)
  //def Directive1[T](name: String)(f: JavaFunction[KappaSession,T]) = new Directive1[T](name, f)
  def Directive2[T1, T2](name: String)(f: KappaSession => (T1, T2)) = new Directive2[T1, T2](name, f)
  //def Directive2[T1, T2](name: String)(f: JavaFunction[KappaSession, (T1, T2)]) = new Directive2[T1, T2](name, f)
  def Directive3[T1, T2, T3](name: String)(f: KappaSession => (T1, T2, T3)) = new Directive3[T1, T2, T3](name, f)
  def Directive4[T1, T2, T3, T4](name: String)(f: KappaSession => (T1, T2, T3, T4)) = new Directive4[T1, T2, T3, T4](name, f)
  def Directive5[T1, T2, T3, T4, T5](name: String)(f: KappaSession => (T1, T2, T3, T4, T5)) = new Directive5[T1, T2, T3, T4, T5](name, f)
}

sealed abstract class Directive protected (val name: String) {

  private var directiveComposedFunction: KappaSession => Directive = _
  private val failureFunctions = mutable.ListBuffer[Throwable ⇒ Directive]()
  private val completeFunctions = mutable.ListBuffer[Directive]()

  def failure(f: Throwable => Directive): this.type = {
    failureFunctions += f
    this
  }

  def failure(f: JavaFunction[Throwable, Directive]): this.type = {
    failureFunctions += f
    this
  }

  def complete(f: Directive): this.type = {
    completeFunctions += f
    this
  }

  protected def compose[T](f: KappaSession => T, s: T => Directive): Directive = {
    directiveComposedFunction = f.andThen(s)
    this
  }

  def apply(implicit session: KappaSession): Response =
    tryApply(session, "success", directiveComposedFunction, session)

  private def tryApply[T](session: KappaSession, state: String, f: T => Directive, arg: T): Response = {
    try {
      val next = f(arg)
      session.trace(s"Applied $state directive $name")
      next.apply(session) match {
        case success: ResponseSuccess => success
        case failure: ResponseFailure =>
          tryFailure(session, failure.error)
          failure
      }
    } catch {
      case NonFatal(e) =>
        session.error(e, s"Error applying $state directive $name")
        tryFailure(session, e)
    } finally {
      tryComplete(session)
    }
  }

  private def tryFailure(session: KappaSession, t: Throwable): Response = {
    failureFunctions.map { ff =>
      tryApply(session, "failure", ff, t)
    }
    ResponseFailure(t)
  }

  private def tryComplete(session: KappaSession): Unit = {
    completeFunctions.map { cd =>
      tryApply[Unit](session, "failure", u => cd, Unit)
    }
  }

}


sealed trait Response


final case class ResponseSuccess(value: String) extends Directive(s"ResponseSuccess($value)") with Response {
  override def apply(implicit session: KappaSession): Response = this
}


final case class ResponseFailure(error: Throwable) extends Directive(s"ResponseFailure($error)") with Response {
  override def apply(implicit session: KappaSession): Response = this
}


final class Directive0 private[kappa] (name: String, f: KappaSession => Any) extends Directive(name) {
  def apply(s: Directive) = compose(f, (_ => s): Any => Directive)
  def success(s: Directive) = apply(s)
}


final class Directive1[T] private[kappa] (name: String, f: KappaSession => T) extends Directive(name) {
  def apply(s: T ⇒ Directive) = compose(f, s)
  //def success(s: T ⇒ Directive) = compose(f, s)
  def success(s: JavaFunction[T, Directive]) = compose(f, s)
}


final class Directive2[T1, T2] private[kappa] (name: String, f: KappaSession => (T1, T2)) extends Directive(name) {
  def apply(s: (T1, T2) ⇒ Directive) = compose(f, s.tupled)
  //def success(s: (T1, T2) ⇒ Directive) = compose(f, s.tupled)
  def success(s: JavaFunction[(T1, T2), Directive]) = compose(f, s)
}


final class Directive3[T1, T2, T3] private[kappa] (name: String, f: KappaSession => (T1, T2, T3)) extends Directive(name) {
  def apply(s: (T1, T2, T3) ⇒ Directive) = compose(f, s.tupled)
  //def success(s: (T1, T2, T3) ⇒ Directive) = compose(f, s.tupled)
}


final class Directive4[T1, T2, T3, T4] private[kappa] (name: String, f: KappaSession => (T1, T2, T3, T4)) extends Directive(name) {
  def apply(s: (T1, T2, T3, T4) ⇒ Directive) = compose(f, s.tupled)
  //def success(s: (T1, T2, T3, T4) ⇒ Directive) = compose(f, s.tupled)
}


final class Directive5[T1, T2, T3, T4, T5] private[kappa] (name: String, f: KappaSession => (T1, T2, T3, T4, T5)) extends Directive(name) {
  def apply(s: (T1, T2, T3, T4, T5) ⇒ Directive) = compose(f, s.tupled)
  def success(s: (T1, T2, T3, T4, T5) ⇒ Directive) = compose(f, s.tupled)
}


/*
import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

// NOTES: Removed directive type param because in java, the compiler
// is not good enough to infer the type through nested directives.
// So response directives always return a generic object Response
sealed trait Directive {

  private val failureFunctions = mutable.ListBuffer[Throwable ⇒ Directive]()
  private val completeFunctions = mutable.ListBuffer[Directive]()

  def name: String

  def failure(f: Throwable => Directive): Directive = {
    failureFunctions += f
    this
  }

  def complete(f: Directive): Directive = {
    completeFunctions += f
    this
  }

  private[kappa] def apply(session: KappaSession): Directive

  private[kappa] def exec(implicit session: KappaSession, state: String): Try[Response] = {
    try {
      val d = apply(session)
      session.trace(s"$state directive $name executed successfully")
      d.exec(session, state) match {
        case success: Success[Response] => success
        case failure: Failure[Response] =>
          tryFailure(failure.exception, session)
          failure
      }
    } catch {
      case NonFatal(error) ⇒
        session.error(error, s"$state directive $name failed")
        tryFailure(error, session)
        Failure(error)
    } finally {
      execComplete(session)
    }
  }

  private def tryFailure(t: Throwable, session: KappaSession): Unit = {
    failureFunctions.map { ff =>
      try {
        val r = ff.apply(t).exec(session, "Failure")
        Success(r)
      } catch {
        case NonFatal(error) ⇒
          session.error(error, s"FAILURE directive $name failed")
          Failure(error)
      }
    }
  }

  private def execComplete(session: KappaSession): Unit = {
    completeFunctions.map { cf =>
      try {
        cf.apply(session).exec(session, "Complete")
      } catch {
        case NonFatal(error) ⇒
          session.error(error, s"COMPLETE directive $name failed")
          throw error
      }
    }
  }

}

object Response {
  val success = Response("_")
}

final case class Response(value: String) extends Directive {
  override def name = s"Response($value)"
  override private[kappa] def exec(implicit session: KappaSession, state: String): Try[Response] = Success(this)
  override private[kappa] def apply(session: KappaSession): Directive = this
  def apply(f: Directive): Directive = this
  override def toString = name
}

final class Directive0(override val name: String, f: KappaSession ⇒ Unit) extends Directive {
  def this() { this("", _ => ()) }
  private var sf: Directive = Response.success
  override private[kappa] def apply(session: KappaSession): Directive = { f(session); sf }
  def apply(f: Directive): Directive0 = { sf = f; this }
  def success(f: Directive): Directive0 = apply(f)
  override def failure(f: Throwable => Directive): Directive = { super.failure(f); this }
  override def complete(f: Directive): Directive0 = { super.complete(f); this }
}

final class Directive1[T1](override val name: String, f: KappaSession ⇒ T1) extends Directive {
  private var sf: T1 ⇒ Directive = (_) ⇒ Response.success
  override private[kappa] def apply(session: KappaSession): Directive = {
    val r = f(session);
    sf.apply(r)
  }
  def apply(f: T1 ⇒ Directive): Directive = { sf = f; this }
  def success(f: T1 ⇒ Directive): Directive = apply(f)
  override def failure(f: Throwable => Directive): Directive1[T1] = { super.failure(f); this }
  override def complete(f: Directive): Directive1[T1] = { super.complete(f); this }
}

final class Directive2[T1, T2](override val name: String, f: KappaSession ⇒ (T1, T2)) extends Directive {
  private var sf: (T1, T2) ⇒ Directive = (_,_) => Response.success
  override private[kappa] def apply(session: KappaSession): Directive = { val r = f(session); sf.apply(r._1, r._2) }
  def apply(f: (T1, T2) ⇒ Directive): Directive = { sf = f; this }
  def success(f: (T1, T2) ⇒ Directive): Directive = apply(f)
  override def failure(f: Throwable => Directive): Directive2[T1, T2] = { super.failure(f); this }
  override def complete(f: Directive): Directive2[T1, T2] = { super.complete(f); this }
}

*/