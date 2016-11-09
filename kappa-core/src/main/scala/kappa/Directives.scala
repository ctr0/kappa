package kappa

import kappa._

/**
  * Created by j0rd1 on 5/11/16.
  */
object Directives {

  def executeCommand(command: String) = new Directive0("executeCommand", { session =>
    // do job
    command.toString
  })


  // Responses

  def response(r: String) = Response(r)

  def debug(t: Throwable) = new Directive0("debug", _.debug(t))

  def debug(msg: String) = new Directive0("debug", _.debug(msg))

  def debug(t: Throwable, msg: String) = new Directive0("debug", _.debug(t, msg))

}


