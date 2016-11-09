package kappa.spark.rest.v1

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.params.HttpClientParams


/**
  *
  * http://spark.apache.org/docs/latest/monitoring.html#rest-api
  *
  */
class RestApiV1(host: String) {

  private val httpClient = {
    val params = new HttpClientParams()
    new HttpClient(params)
  }

  private val mapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper
  }

  private def get(resource: String, params: (String, Any)*) = {
    val url = s"$host/api/v1/$resource?${
      params.map { case (k, v) =>
        s"$k=$v"
      }.mkString("&")
    }"
    val method = new GetMethod(url)
    val responseCode = httpClient.executeMethod(method)
    // FIXME response errors
    method.getResponseBodyAsStream
  }

  def getApplications(params: (String, Any)*) = {
    mapper.readValue[Seq[Application]](get("applications", params: _*))
  }

  def getApplication(id: String, params: (String, Any)*) = {
    mapper.readValue[Application](get(s"applications/$id", params: _*))
  }
/*
  def getAppJobs(id: String, params: (String, Any)*) = {
    mapper.readValue[Seq[Job]](get(s"applications/$id/jobs", params: _*))
  }

  def getAppJob(id: String, job: String, params: (String, Any)*) = {
    mapper.readValue[Job](get(s"applications/$id/job/$job", params: _*))
  }

  def getAppStages(id: String, params: (String, Any)*) = {
    mapper.readValue[Seq[Stage]](get(s"applications/$id/stages", params: _*))
  }

  def getAppStage(id: String, stage: String, params: (String, Any)*) = {
    mapper.readValue[Stage](get(s"applications/$id/stages/$stage", params: _*))
  }

  def getAppStageAttempt(id: String, stage: String, attempt: String, params: (String, Any)*) = {
    mapper.readValue[Application](get(s"applications/$id/stages/$stage/$attempt", params: _*))
  }

  def getAppStageAttemptTaskSummary(id: String, stage: String, attempt: String, params: (String, Any)*) = {
    mapper.readValue[???](get(s"applications/$id/stages/$stage/$attempt/taskSummary", params: _*))
  }

  def getAppStageAttemptTasks(id: String, stage: String, attempt: String, params: (String, Any)*) = {
    mapper.readValue[???](get(s"applications/$id/stages/$stage/$attempt/taskList", params: _*))
  }
*/

}
