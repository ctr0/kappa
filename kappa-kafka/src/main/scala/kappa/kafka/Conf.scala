package kappa.kafka

import kappa.{KappaConf, KappaSession}

/**
  * Created by j0rd1 on 7/11/16.
  */
object Conf {

  val `kappa.kafka.session.timeout` = "kappa.kafka.session.timeout" -> 1000
  val `kappa.kafka.connection.timeout` = "kappa.kafka.connection.timeout" -> 1000

}
