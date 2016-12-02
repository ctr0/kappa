package kappa

/*

/**
  * Created by j0rd1 on 9/11/16.
  */

package object macros {

  import scala.language.experimental.macros

  def DirectiveM0(f: KappaSession ⇒ Unit): DirectiveM0 = macro Macros.directive0ApplyImpl //new Directive0(f)

  def DirectiveM1[T1](f: KappaSession ⇒ T1): DirectiveM1[T1] = macro Macros.directive1Impl[T1] //new Directive1[T1](f)

  def DirectiveM2[T1, T2](f: KappaSession ⇒ (T1, T2)): DirectiveM2[T1, T2] = macro Macros.directive2Impl[T1, T2] //new Directive2[T1, T2](f)

  def dummyDirectiveM() = DirectiveM0("dummyDirectiveM") { session =>
    session.info("macro works!")
  }
}
*/