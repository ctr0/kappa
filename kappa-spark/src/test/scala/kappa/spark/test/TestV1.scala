package kappa.spark.test

import kappa.spark.rest.v1._

/**
  * Created by j0rd1 on 7/11/16.
  */
object TestV1 {

  def main(args: Array[String]): Unit = {
    val client = new WebUiApiV1("http://localhost:4040")

    val apps: Seq[Application] = client.getApplications()
    apps.foreach(println)
  }
}
