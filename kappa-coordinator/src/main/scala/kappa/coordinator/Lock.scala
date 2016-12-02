package kappa.coordinator

import java.time.Instant
import java.util.concurrent.TimeUnit

import kappa._
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Created by j0rd1 on 6/11/16.
  */
trait ReadLock {

  def assertLocked(path: Path): Directive0

  //def assertLocked(path: String): Directive0

  def assertNotLocked(path: Path): Directive0

  //def assertNotLocked(path: String): Directive0

  def toWriteLock: Directive1[WriteLock]

}

/**
  * Created by j0rd1 on 5/11/16.
  */
trait WriteLock extends ReadLock {

  private lazy val instant = Instant.now()

  def id: String = instant.toString

  def write(path: Path): Directive0

  //def write(path: String): Directive0

}

/**
  * Created by j0rd1 on 4/11/16.
  */
object Lock {

  val ROOT_LOCK_PATH = "/kappa/locks"
  val ROOT_DOMAIN_PATH = "/kappa/domains"
  val DEFAULT_ACQUIRE_LOCK_TIMEOUT = 3000
}

/**
  * Created by j0rd1 on 4/11/16.
  */
private class Lock (session: KappaSession, basePath: String) extends WriteLock with ReadLock {

  import Lock._

  private val internalDomainPath = Path(ROOT_DOMAIN_PATH, basePath)
  private val internalLockPath = Path(ROOT_LOCK_PATH, basePath)
  private val internalLock = new InterProcessReadWriteLock(session.zkClient, internalLockPath)

  private lazy val instant = Instant.now()


  override def id: String = instant.toString

  override def write(path: Path) = Directive0(s"write($path)") { session =>
    session.createPathIfNotExists(internalDomainPath/path)
  }

  //def write(path: String) = Directive0 { session =>
  //  session.createPathIfNotExists(internalDomainPath/path)
  //}

  //override def assertLocked(path: Path) = assertLocked(path.value)

  override def assertLocked(path: Path) =
      Directive0(s"assertLocked($path)") { session =>
    if (session.checkExistsPath(internalDomainPath/path) == null) {
      throw new Exception(s"path ${internalDomainPath/path} is not locked")
    }
  }

  //override def assertNotLocked(path: Path) = assertNotLocked(path.value)

  override def assertNotLocked(path: Path) = Directive0(s"assertNotLocked($path)") { session =>
    if (session.checkExistsPath(internalDomainPath/path) != null) {
      throw new Exception(s"path ${internalDomainPath/path} is locked")
    }
  }

  override def toWriteLock = Directive1[WriteLock](s"toWriteLock") { _ =>
    releaseReadLock()
    acquireWriteLock()
    this
  }

  private[coordinator] def writeLock() = Directive1[WriteLock]("writeLock") { session =>
      acquireWriteLock() match {
        case Success(_) ⇒ session.debug(s"Acquired write lock $internalLockPath")
        case Failure(error) ⇒ throw error
      }
      this.asInstanceOf[WriteLock]
    } complete {
      Directive0("releaseWriteLock") { session =>
        releaseWriteLock().recover {
          case t ⇒
            session.debug(t, s"Error releasing write lock on $internalLockPath (Ignoring error)")
        }
      }
    }

  private[coordinator] def readLock() = Directive1[ReadLock]("readLock") { session =>
    acquireReadLock() match {
      case Success(_) ⇒ session.debug(s"Acquired read lock $internalLockPath")
      case Failure(error) ⇒ throw error
    }
    this.asInstanceOf[ReadLock]
  } complete {
    Directive0("releaseReadLock") { session =>
      releaseReadLock().recover {
        case t ⇒
          session.debug(t, s"Error releasing read lock on $internalLockPath (Ignoring error)")
      }
    }
  }

  private def acquireWriteLock(): Try[Unit] = {
    Try {
      internalLock.writeLock().acquire(DEFAULT_ACQUIRE_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)
    } match {
      case Success(success) => if (success) Success(()) else Failure {
          CoordinatorException(getClass, basePath, s"Could not acquire write lock $internalLockPath")
        }
      case Failure(error) ⇒ Failure {
        CoordinatorException(getClass, basePath, s"Could not acquire write lock $internalLockPath", error)
      }
    }
  }

  private def releaseWriteLock(): Try[Unit] = {
    Try {
      if (internalLock.writeLock().isAcquiredInThisProcess)
        internalLock.writeLock().release()
    } match {
      case Success(_) ⇒ Success(())
      case Failure(error) ⇒ Failure {
        CoordinatorException(getClass, basePath, s"Could not release write lock $internalLockPath", error)
      }
    }
  }

  private def acquireReadLock(): Try[Unit] = {
    Try {
      internalLock.readLock().acquire(DEFAULT_ACQUIRE_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)
    } match {
      case Success(success) => if (success) Success(()) else Failure {
        CoordinatorException(getClass, basePath, s"Could not acquire read lock $internalLockPath")
      }
      case Failure(error) ⇒ Failure {
        CoordinatorException(getClass, basePath, s"Could not acquire read lock $internalLockPath", error)
      }
    }
  }

  private def releaseReadLock(): Try[Unit] = {
    Try {
      if (internalLock.readLock().isAcquiredInThisProcess)
        internalLock.readLock().release()
    } match {
      case Success(_) ⇒ Success(())
      case Failure(error) ⇒ Failure {
        CoordinatorException(getClass, basePath, s"Could not release read lock $internalLockPath", error)
      }
    }
  }

}
