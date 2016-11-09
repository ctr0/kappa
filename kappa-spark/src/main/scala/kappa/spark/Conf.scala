package kappa.spark

/**
  * Created by j0rd1 on 8/11/16.
  */
object Conf {

  val `kappa.spark.master` = ("kappa.spark.master", "local[*]")
  val `kappa.spark.deploy-mode` = ("kappa.spark.deploy-mode", "cluster")
  val `kappa.spark.resource.$name`: String => (String, String) = name => (s"kappa.spark.resource.$name", "<resource>")
}
