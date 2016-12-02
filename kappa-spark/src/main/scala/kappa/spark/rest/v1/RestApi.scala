package kappa.spark.rest.v1

import java.io.InputStream

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.{GetMethod, PostMethod, StringRequestEntity}
import org.apache.commons.httpclient.params.HttpClientParams

/**
  * Created by j0rd1 on 2/12/16.
  */
abstract class RestApi(url: String) {

  private val httpClient = {
    val params = new HttpClientParams()
    new HttpClient(params)
  }

  protected val mapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper
  }

  private def endpoint(resource: String, params: Seq[(String, Any)]): String = {
    s"$url/$resource?${
      params.map { case (k, v) =>
        s"$k=$v"
      }.mkString("&")
    }"
  }

  protected def get(resource: String, params: (String, Any)*): InputStream = {
    val method = new GetMethod(endpoint(resource, params))
    val responseCode = httpClient.executeMethod(method)
    // FIXME response errors
    method.getResponseBodyAsStream
  }

  protected def post(resource: String, content: String, params: (String, Any)*): InputStream = {
    val method = new PostMethod(endpoint(resource, params))
    val entity = new StringRequestEntity(content, "application/json", "UTF-8")
    method.setRequestEntity(entity)
    method.setRequestHeader("Accept", "application/json")
    method.setRequestHeader("Content-type", "application/json")
    val responseCode = httpClient.executeMethod(method)
    // FIXME response errors
    method.getResponseBodyAsStream
  }

}
