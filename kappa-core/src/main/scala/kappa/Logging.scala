package kappa

//import java.util.logging.{Level, Logger}

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
  * Created by j0rd1 on 5/11/16.
  */
trait Logging {

  val logger = LoggerFactory.getLogger(classOf[Logging])


  def info(msg: String): Unit = info(null, msg)

  def info(t: Throwable): Unit = info(t, "")

  def info(t: Throwable, msg: String): Unit = logger.info(msg, t)


  def error(msg: String): Unit = error(null, msg)

  def error(t: Throwable): Unit = error(t, "")

  def error(t: Throwable, msg: String): Unit = logger.error(msg, t)


  def debug(msg: String): Unit = debug(null, msg)

  def debug(t: Throwable): Unit = debug(t, "")

  def debug(t: Throwable, msg: String): Unit = logger.debug(msg, t)


  def trace(msg: String): Unit = trace(null, msg)

  def trace(t: Throwable): Unit = trace(t, "")

  def trace(t: Throwable, msg: String): Unit = logger.trace(msg, t)

}
