#!/usr/bin/env bash


curl -X POST http://ctro:6066/v1/submissions/create --header "Content-Type:application/json;charset=UTF-8" --data '{
  "action" : "CreateSubmissionRequest",
  "appArgs" : [ "myAppArgument1" ],
  "appResource" : "file:/home/j0rd1/Development/Projects/kappa/spark-mock/out/artifacts/spark_mock/spark-mock.jar",
  "clientSparkVersion" : "2.0.0",
  "environmentVariables" : {
    "SPARK_ENV_LOADED" : "1"
  },
  "mainClass" : "spark.mock.Driver",
  "sparkProperties" : {
    "spark.jars" : "file:/home/j0rd1/Development/Projects/kappa/spark-mock/out/artifacts/spark_mock/spark-mock.jar",
    "spark.driver.supervise" : "false",
    "spark.app.name" : "SparkMock",
    "spark.eventLog.enabled": "true",
    "spark.submit.deployMode" : "cluster",
    "spark.master" : "spark://ctro:6066"
  }
}'