package kappa.coordinator.recipes

import kappa._
import kappa.coordinator._

import scala.util.Try

/**
  * Created by j0rd1 on 5/11/16.
  */
class BatchJobUnique extends Spec {

  override def session: KappaSession = null
  override def domain: Path = "eci/offline"
  def name: Path = "full-index"
/*
  def startSend(): Try[LockId] = {
    lockOnSuccess("send-start") { lock =>
      lock.assertNotLocked(".") {
        createKafkaTopic(name/lock.id) {
          success {
            executeCommand(s"spark-job-${name/lock.id}") {
              succes(name/lock.id)
            }
          } failure ("error") {
            deleteKafkaTopic("")
          }
        }
      }
    }
  }

  def send(id: String, msgs: Seq[String]): Try[Unit] = {
    lockAndRelease("sending") { lock =>
      lock.assertrunning("send-start") {
        // transactional directives
        sendKafkaMessages(topic = id, msgs) { futureOffsets =>
          futureOffsets
            .onSucess(sucess())
            .onFailure(failure())
        }
      }
    }
  }

  def endSend(id: String): Try[Unit] = {
    lockOnSuccess("process") { lock =>
      withLock(lock) {
        lock.assertRunnning("send") {
          waitNotLocked("sending", 10000) {
            retrieveKafkaOffsets(topic = id) { offsets =>
              storeData(offsets) {
                executeCommand("spark-job", offsets) {
                  success()
                }
              }
            }
          }
        }
      }
    }
  }
*/
}
