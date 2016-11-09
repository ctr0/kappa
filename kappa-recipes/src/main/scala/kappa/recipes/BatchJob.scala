package kappa.coordinator.recipes

import kappa._
import kappa.coordinator._

import scala.util.Try

/**
  * Created by j0rd1 on 1/11/16.
  */
object BatchJob extends Job {

  def domain = Path("eci")

  def name = Path("offline/full_index")

  implicit def session: KappaSession = null

  /*
  val sendStart: Process = {
    lock("send") { procId =>
      createKafkaTopic(procId.toString) {
        waitForCompletion()
      }
    } assert {
      notRunning(basePath/"offline")
    } rollback { procId =>
      deleteKafkaTopicIfExists(procId.asString)
    }
  }

  val sendEnd: String => Process = { id =>
    lock("process") { _ =>
      //completeLock("send") {
      storeKafkaOffsets(id, jobPath) { offsets =>
        executeCommand("run-batch-job") {
          waitForProgress()
        }
      }
      //}
    } assert {
      running(jobPath/"send")
    } rollback { _ =>
      complete()
    }
  }

  val send: (String, Seq[String]) ⇒ Process = { (id, messages)
//    process {
//      sendKafkaMessages(topic = id, messages)
//    } assert {
//      running(jobPath/"send")
//    }
  }

  def status(id: String): Unit = {
    lockStatus(id).foldLeft("stopped") { (status, lockId) =>
      lockId.name
    }
  }

  val sendStart2: Directive = {
    implicit val domain: Domain = null

    lockOnSuccess("lockPath") { lockId =>
      assertRunning("") {
        null
//        createKafkaTopic(procId.toString) {
//          sucess(procId)
//        }
      }
    } rollback { lockId =>
      null
    }
  }

  val send2: (String, Seq[String]) ⇒ Process = { (id, messages) =>
    assertRunning(id / "send") {
      sync("localPath", "sending") {

      }
    }

  }

  val sendEnd2: String => Process = { id =>
    lock("domain", "process") { _ =>
      assertRunning("send") {

      }
      //completeLock("send") {
      storeKafkaOffsets(id, jobPath) { offsets =>
        executeCommand("run-batch-job") {
          waitForProgress()
        }
      }
      //}
    } assert {
      running(jobPath/"send")
    } rollback { _ =>
      complete()
    }
  }

*/

}
