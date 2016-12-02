package kappa.macros

import kappa._

/**
  * Created by j0rd1 on 9/11/16.
  */
/*
object Macros {

  import scala.language.experimental.macros
  import scala.reflect.macros.blackbox

  def directive0ApplyImpl(c: blackbox.Context)
      (f: c.Expr[KappaSession => Unit], s: c.Expr[DirectiveM]): c.Expr[DirectiveM0] = {

    import c.universe._
    //val owner = c.internal.enclosingOwner
    //val builder = new StringBuilder(owner.toString)
    //if (owner.isMethod) {
    //  owner.asMethod.paramLists // TODO params
    //}
    //c.Expr(q"""new Directive0(${builder.toString()}, ${f.tree})""")
    c.Expr {
      q"""{
        val f = ${f.tree}
        val s = ${s.tree}
        try {
          f.apply
          session.trace(s"Applied $$state directive $$name")
        }
      }"""
    }
  }

  def directive1Impl[T1](c: blackbox.Context)(f: c.Expr[KappaSession ⇒ T1]): c.Expr[Directive1[T1]] = {
    import c.universe._
    val name = c.internal.enclosingOwner.toString
    c.Expr(q"""new Directive1($name, ${f.tree})""")
  }

  def directive2Impl[T1, T2](c: blackbox.Context)(f: c.Expr[KappaSession ⇒ (T1, T2)]): c.Expr[Directive2[T1, T2]] = {
    import c.universe._
    val name = c.internal.enclosingOwner.toString
    c.Expr(q"""new Directive2($name, ${f.tree})""")
  }

}
*/