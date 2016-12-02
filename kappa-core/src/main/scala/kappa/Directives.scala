package kappa

import kappa._

/**
  * Created by j0rd1 on 5/11/16.
  */
object Directives {

  def executeCommand(command: String) = Directive0("executeCommand") { session =>
    // do job
    command.toString
  }


  // Responses

  def response(r: String) = ResponseSuccess(r)

  def debug(t: Throwable) = Directive0("debug")(_.debug(t))

  def debug(msg: String) = Directive0("debug")(_.debug(msg))

  def debug(t: Throwable, msg: String) = Directive0("debug")(_.debug(t, msg))

}


