package kappa.coordinator.recipes

import kappa._
import kappa.coordinator._


import scala.util.Try

/**
  * Created by j0rd1 on 5/11/16.
  */
class BatchJobConcurrent extends Spec {

  override def session: KappaSession = null
  override def domain: Path = "eci/offline/full-index"
  /*
    def test() = {
      createKafkaTopic2("") { s =>
        response(s)
      }
    }

    def startSend() = {
      writeLock { lock =>
        createKafkaTopic(domain/lock.id) success {
          success {
            lock.write(domain/lock.id/"send-start") {
              response(lock.id)
            }
          } failure { t =>
            executeCommand(domain/lock.id) {
              response(t.getLocalizedMessage)
            }
          }
        }
      }
    }
    */
/*
  def send(id: String, msgs: Seq[String]): Directive[Unit] = {
    readLock(id) { lock =>
      success {
        lock.assertLocked(domain/id/"send-start") {
          success {
            lock.assertNotLocked(domain/id/"process") {
              val writeLock = lock.toWriteLock
              writeLock.writeWithIndex(domain/id/"sending") { i =>
                sendKafkaMessages(topic = id, msgs) { futureOffsets =>
                  futureOffsets
                    .onSucess(writeLock.deleteWithIndex(domain/id/"sending", i))
                    .onFailure(failure())
                  response(())
              }
            }
          } failure (t ⇒ response("The sending process has not ben started"))
        }
      } failure (t ⇒ response("The sending process has not ben started"))
    }
  }

  def endSend(id: String): Try[Unit] = {
    readLock { lock ⇒
      lock.assertLocked(id/"send-start") {
        lock.waitUntilUnlock(id/"sending", timeout = 10000) {
          retrieveKafkaOffsets(topic = id) { offsets =>
            lock.toWriteLock { lock ⇒
              storeData(name/id/"offsets") {
                response(())
              }
            }
          }
        }
      }
    }
  }
*/
}
