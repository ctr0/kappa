package kappa


import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.util.control.NonFatal

// NOTES: Removed directive type param because in java, the compiler
// is not good enough to infer the type through nested directives.
// So response directives always return a generic object Response
sealed trait Directive {

  private val failureFunctions = ArrayBuffer[Throwable ⇒ Directive]()
  private val completeFunctions = ArrayBuffer[Directive]()

  def name: String

  private[kappa] def apply(session: KappaSession): Directive

  def execute()(implicit session: KappaSession): Result = {
    try {
      session.trace(s"Executing directive $name")
      apply(session).execute()(session)
    } catch {
      case t: Throwable ⇒
        onFailure(t, session)
        throw t
    } finally {
      onComplete(session)
    }
  }

  def failure(f: Throwable => Directive): Directive = {
    failureFunctions += f
    this
  }

  private def onFailure(t: Throwable, session: KappaSession): Unit = {
    session.trace(t, s"Error applying directive $name, executing rollback")
    failureFunctions.foreach { ff =>
      try {
        ff.apply(t).onFailure(t, session)
      } catch {
        case NonFatal(e) ⇒
          session.error(e, s"Error applying failure function $ff")
      }
    }
  }

  def complete(f: Directive): Directive = {
    completeFunctions += f
    this
  }

  def onComplete(session: KappaSession): Unit = {
    session.trace(s"Executing complete directive $name")
    completeFunctions.foreach { cf =>
      try {
        cf.apply(session).onComplete(session)
      } catch {
        case NonFatal(e) ⇒
          session.error(e, s"Error applying complete function $cf")
      }
    }
  }

}

object Result {
  val success = Response("_")
  def apply(r: String) = Response(r)
  def apply(t: Throwable) = Error(t)
}

sealed abstract class Result extends Directive {
  override def execute()(implicit session: KappaSession) = this
  override private[kappa] def apply(session: KappaSession): Directive = this
  def apply(f: Directive): Directive = this
  override def toString = name
}

final case class Response(value: String) extends Result {
  override def name = s"Success($value)"
}

final case class Error(error: Throwable) extends Result {
  override def name = s"Failure($error)"
}

final class Directive0(override val name: String, f: KappaSession ⇒ Unit) extends Directive {
  def this() { this("", _ => ()) }
  private var sf: Directive = Result.success
  override private[kappa] def apply(session: KappaSession): Directive = { f(session); sf(session) }
  def apply(f: Directive): Directive0 = { sf = f; this }
  def success(f: Directive): Directive0 = apply(f)
  override def failure(f: Throwable => Directive): Directive = { super.failure(f); this }
  override def complete(f: Directive): Directive0 = { super.complete(f); this }
}

final class Directive1[T1](override val name: String, f: KappaSession ⇒ T1) extends Directive {
  private var sf: T1 ⇒ Directive = (_) ⇒ Result.success
  override private[kappa] def apply(session: KappaSession): Directive = { val r = f(session); sf.apply(r) }
  def apply(f: T1 ⇒ Directive): Directive = { sf = f; this }
  def success(f: T1 ⇒ Directive): Directive = apply(f)
  override def failure(f: Throwable => Directive): Directive1[T1] = { super.failure(f); this }
  override def complete(f: Directive): Directive1[T1] = { super.complete(f); this }
}

final class Directive2[T1, T2](override val name: String, f: KappaSession ⇒ (T1, T2)) extends Directive {
  private var sf: (T1, T2) ⇒ Directive = (_,_) => Result.success
  override private[kappa] def apply(session: KappaSession): Directive = { val r = f(session); sf.apply(r._1, r._2) }
  def apply(f: (T1, T2) ⇒ Directive): Directive = { sf = f; this }
  def success(f: (T1, T2) ⇒ Directive): Directive = apply(f)
  override def failure(f: Throwable => Directive): Directive2[T1, T2] = { super.failure(f); this }
  override def complete(f: Directive): Directive2[T1, T2] = { super.complete(f); this }
}
