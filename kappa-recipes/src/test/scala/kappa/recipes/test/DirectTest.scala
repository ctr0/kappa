package kappa.recipes.test

import kappa._
import kappa.coordinator._
import kappa.Directives._

/**
  * Created by j0rd1 on 10/11/16.
  */
class DirectTest extends Spec {

  val conf = new KappaConf

  override implicit def session: KappaSession = new KappaSession(conf)

  override def domain: Path = "direct_test"

  val start = {
    writeLock() { lock =>
      lock.assertNotLocked("start") {
        executeCommand("") {
          lock.write(s"start-${lock.id}")
        }
      }
    }
  }



}
