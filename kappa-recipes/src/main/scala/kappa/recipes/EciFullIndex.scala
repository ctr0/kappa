package kappa.coordinator.recipes

import kappa._
import kappa.coordinator._

import scala.util.Try

/**
  * Created by j0rd1 on 1/11/16.
  *
class EciFullIndex(site: String) extends JobProcess {

  implicit val kc: KappaContext = null

  override val jobPath = "offline/full_index"

  def startSending(): Try[ProcId] = {
    lock("send") { procId =>
      createKafkaTopic(procId.toString) {
        executeCommand("start-spark-streaming") {
          waitForCompletion("end")
        }
      }
    } claim {
      Path(site, "offline")
    } rollback { maybeProcId =>
      if (maybeProcId.isDefined) {
        deleteKafkaTopicIfExists(maybeProcId.get.asString)
      }
    }
  }

  def send(id: String, msg: String): Unit = {

  }

  def endSending(id: String): Unit = {
    lock("progress") { _ =>
      storeKafkaOffsets(id, jobPath) { offsets =>
        completeLock("send") {
          waitForProgress()
        }
      }
    } rollback { _ =>
    }
  }

  def status(id: String): Unit = {
    lockStatus(id).foldLeft("stopped") { (status, lockId) =>
      lockId.name
    }
  }

}
*/