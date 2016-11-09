package kappa

object KappaConf {

}

/**
  * Created by j0rd1 on 27/10/16.
  */
class KappaConf {

  val zkUrl = "localhost:2181"

  val zkAuthorization: Option[(String, String)] = None

  private val properties = Map[String, Any]()

  def getJobConf(group: String, job: String): Any = ???

  def apply[T](key: String, defaultValue: T): T = properties.getOrElse(key, defaultValue).asInstanceOf[T]

  def apply[T](kv: (String, T)): T = properties.getOrElse(kv._1, kv._2).asInstanceOf[T]

  //sdef apply[T >: Any](key: String): T = properties(key).asInstanceOf[T]

  def asMap: Map[String, Any] = properties

}