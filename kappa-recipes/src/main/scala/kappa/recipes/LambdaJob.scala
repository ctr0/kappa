package kappa.coordinator.recipes

import kappa.coordinator._

import scala.util.Try

/**
  * Created by j0rd1 on 1/11/16.
  *
class LambdaJob extends JobProcess {

  implicit val kc: KappaContext = null

  override def path = "lambda-job"

  def start(): Try[ProcId] = {
    Lock("start") { procId =>
      createKafkaTopic(procId.toString) {
        executeCommand("start-spark-streaming") {
          Complete()
        }
      }
    } rollback { procId =>
      deleteKafkaTopicIfExists(procId.toString)
    }
  }

  def send(id: String, msg: String): Unit = {

  }

  def reprocess(id: String): Try[ProcId] = {
    Lock("end") { procId =>
      executeCommand("start-spark-batch from first offset") {
        Complete()
      }
    } rollback { procId =>
      executeCommand("kill-spark-batch") {
        Complete()
      }
    }
  }

}
*/