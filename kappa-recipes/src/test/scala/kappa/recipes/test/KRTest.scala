package kappa.recipes.test

import kappa._
import kappa.Directives._
import kappa.coordinator._
import kappa.kafka.Directives._
import kappa.spark.Directives._

import scala.concurrent.Future

/**
  * Created by j0rd1 on 5/11/16.
  */
object KRTest extends Spec {

  val conf = new KappaConf

  override implicit def session: KappaSession = new KappaSession(conf)

  override def domain: Path = "test"

  def krtTest() = {
    writeLock() { lock =>
      lock.assertNotLocked("krt-test") {
        createKafkaTopicIfNotExists("topic-name", 2, 1) {
          submitSparkJob("job-name", "resource", null) {
            lock.write("krt-test") {
              response("lock -> " + lock.id)
            } failure { t =>
              killSparkJob("job-name")
            }
          } failure { t =>
            deleteKafkaTopic("topic-name")
          }
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {


    val test = krtTest()
    val response = test.apply

    println("RESPONSE: " + response)
  }

}
