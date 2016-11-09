package kappa.spark.rest

/**
  * Created by j0rd1 on 7/11/16.
  */
package object v1 {

  trait Status

  case object Completed extends Status

  case object Running

  /*
  {
    "id" : "local-1478535602760",
    "name" : "ECIKafkaSpark",
    "attempts" : [{
      "startTime" : "2016-11-07T16:19:59.085GMT",
      "endTime" : "1969-12-31T23:59:59.999GMT",
      "lastUpdated" : "2016-11-07T16:19:59.085GMT",
      "duration" : 0,
      "sparkUser" : "",
      "completed" : false,
      "endTimeEpoch" : -1,
      "startTimeEpoch" : 1478535599085,
      "lastUpdatedEpoch" : 1478535599085
    }]
  }
  */
  case class Application(id: String, name: String, attempts: Seq[Attempt])

  case class Attempt(
    startTime: String, endTime: String, lastUpdated: String, duration: Int, sparkUser: String,
    completed: Boolean, endTimeEpoch: Long, startTimeEpoch: Long, lastUpdatedEpoch: Long
  )


}
