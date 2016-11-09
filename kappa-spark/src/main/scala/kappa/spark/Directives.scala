package kappa.spark

import java.io.File

import kappa.Directives._
import kappa._
import kappa.spark.rest.v1.RestApiV1
import org.apache.commons.lang.text.StrSubstitutor
import org.apache.spark.deploy.Client
import org.apache.spark.launcher.SparkLauncher

import scala.collection.JavaConverters.{getClass => _, _}

/**
  * Created by j0rd1 on 8/11/16.
  */
object Directives {

  import Conf._

  def submitSparkJob(id: String, resource: String, conf: String) =
      Directive0(s"submitSparkJob($id)") { session =>
    val launcher = new SparkLauncher()
    //.setJavaHome() use system
    //.setSparkHome() use system
      .setMaster(session.conf(`kappa.spark.master`))
      .setDeployMode(session.conf(`kappa.spark.deploy-mode`))
      .setAppName(id)
      .setAppResource(resource)
      .setPropertiesFile(conf)
    //.setConf() through properties file
    //.setVerbose(true)
    launcher.startApplication() // FIXME listeners
  }

  def killSparkJob(id: String) = Directive0(s"killSparkJob($id)") { session =>
    // FIXME exception
    Client.main(Array(
      "kill",
      session.conf(`kappa.spark.master`),
      id
    ))
  }

  def killSparkJobIfExists(id: String) = Directive0(s"killSparkJobIfExists($id)") { session =>
    Client.main(Array(
      "kill",
      session.conf(`kappa.spark.master`),
      id
    ))
  }

  /*
  private def COMMAND_getCommand(session: KappaSession, script: String, args: String*): String = {
    import scala.collection.JavaConverters._
    new StrSubstitutor(session.conf.asMap.asJava).replace { // FIXME args
      session.getOrCreate(s"kappa.spark.script.`$script`") {
        getClass.getResourceAsStream(script)
      }
    }
  }

  def COMMAND_submitSparkJob(id: String, resource: String, conf: String) = Directive0 { session =>
    executeCommand(getCommand(session, "/spark-submit-job.sh", resource))
  }

  def COMMAND_killSparkJob(id: String) = Directive0 { session =>
    val restApi = session.getOrCreate(classOf[RestApiV1]) {
      new RestApiV1("http://localhost:4040") // FIXME url
    }
    restApi.getApplications("status" -> "running").find(_.name == id) match {
      case Some(app) =>
        executeCommand(getCommand(session, "/spark-kill-job.sh", app.id))
      case None =>
        throw new Exception(s"Spark application $id is not running") // FIXME Exception

    }
  }
  */

}
