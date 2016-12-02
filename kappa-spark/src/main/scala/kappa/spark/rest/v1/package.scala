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

  /*
  curl -X POST http://spark-cluster-ip:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
  {
    "action" : "CreateSubmissionRequest",
    "appArgs" : [ "myAppArgument1" ],
    "appResource" : "file:/myfilepath/spark-job-1.0.jar",
    "clientSparkVersion" : "1.5.0",
    "environmentVariables" : {
      "SPARK_ENV_LOADED" : "1"
    },
    "mainClass" : "com.mycompany.MyJob",
    "sparkProperties" : {
      "spark.jars" : "file:/myfilepath/spark-job-1.0.jar",
      "spark.driver.supervise" : "false",
      "spark.app.name" : "MyJob",
      "spark.eventLog.enabled": "true",
      "spark.submit.deployMode" : "cluster",
      "spark.master" : "spark://spark-cluster-ip:6066"
    }
  }
   */
  case class Submission(action: String, appArgs: Seq[String], appResource: String, clientSparkVersion: String,
      environmentVariables: Map[String, String], mainClass: String, sparkProperties: Map[String, String]
  )

  /*
  {
    "action" : "CreateSubmissionResponse",
    "message" : "Driver successfully submitted as driver-20151008145126-0000",
    "serverSparkVersion" : "1.5.0",
    "submissionId" : "driver-20151008145126-0000",
    "success" : true
  }
   */
  case class SubmissionResponse(action: String, message: String, serverSparkVersion: String,
    submissionId: String, success: Boolean)

  /*
  curl http://spark-cluster-ip:6066/v1/submissions/status/driver-20151008145126-0000
  {
    "action" : "SubmissionStatusResponse",
    "driverState" : "FINISHED",
    "serverSparkVersion" : "1.5.0",
    "submissionId" : "driver-20151008145126-0000",
    "success" : true,
    "workerHostPort" : "192.168.3.153:46894",
    "workerId" : "worker-20151007093409-192.168.3.153-46894"
  }
   */
  case class StatusResponse(action: String, driverState: String, serverSparkVersion: String, submissionId: String,
    success: Boolean, workerHostPort: String, workerId: String)

  /*
  curl -X POST http://spark-cluster-ip:6066/v1/submissions/kill/driver-20151008145126-0000
  {
    "action" : "KillSubmissionResponse",
    "message" : "Kill request for driver-20151008145126-0000 submitted",
    "serverSparkVersion" : "1.5.0",
    "submissionId" : "driver-20151008145126-0000",
    "success" : true
  }
   */
  case class KillResponse(action: String, message: String, serverSparkVersion: String, submissionId: String,
    success: Boolean)

}
