package kappa.coordinator

import java.time.Instant

import kappa.Directive

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}


/**
  * Created by j0rd1 on 4/11/16.
  */
private object ProcessLock {

  private val ACQUIRE_LOCAL_LOCK_TIMEOUT = 3000

  private [coordinator] val LOCK_ZNODE_NAME = "_kappa_local_lock"

}


/**
  * Created by j0rd1 on 4/11/16.
  *
private class ProcessLock (clazz: Class[_], path: String, lockFunction: LockId => Directive)
                          (implicit kc: Lock) extends Process with Lock {

  private var rollbackFunction: LockId => Directive = _

  private var claimPaths: Seq[String] = Seq(path)

  private lazy val localLock: LocalLock = new LocalLock(kc.session.zkClient, path)

  override def claim(paths: String*): Lock = {
    claimPaths ++= paths
    this
  }

  override def assert(assertion: Assertion): Lock = ???

  override def rollback(func: LockId => Directive): Process = {
    rollbackFunction = func
    this.asInstanceOf[Process]
  }

  override def run(): Try[LockId] = {
    kc.acquireWriteLock() match {
      case Failure(error) ⇒ return Failure(error)
      case Success(_) ⇒
    }
    kc.session.checkExistsPaths(claimPaths) match {
      case Success(pathAndStat) ⇒ if (pathAndStat.nonEmpty) {
        kc_releaseGlobalWriteLock()
        Failure(null) // Fixme
      } else {
        executeLock(LockId(path, Instant.now()))
      }
      case Failure(error) ⇒
        kc_releaseGlobalWriteLock()
        failure("Error checking claimed paths, process cannot be started", error)
    }
  }

  private def executeLock(lockId: LockId): Try[LockId] = {
    try {
      lockFunction(lockId)
      Success(lockId)
    } catch {
      case NonFatal(error) ⇒
        kc_releaseGlobalWriteLock()
        executeRollback(lockId) match {
          // TODO Replace [NODE]
          case Success(_) =>
            failure(s"Error executing [NODE] directives. Rollback executed successfully.", error)
          case Failure(e) =>
            failure(s"Error executing [NODE] directives. Rollback failed with error (see log files for details): $e", e)
        }
    }
  }

//  private def executeDirectives(lockId: LockId): Try[LockId] = {
//    kc.acquireLocalLock(localLock) match {
//      case Failure(error) ⇒ return Failure(error)
//      case Success(_) ⇒
//    }
//
//    null
//  }

  private def executeRollback(lockId: LockId): Try[_] = {
    try {
      rollbackFunction(lockId)
      Success(())
    } catch {
      case NonFatal(error) ⇒
        // TODO log error
        Failure(error)
    }
  }

  @inline
  private def kc_releaseGlobalWriteLock(): Unit  = {
    kc.releaseRootWriteLock().recover {
      case _ ⇒ // TODO log _
    }
  }

    private [coordinator] def rollbackAndResetLock(lockId: Option[LockId]): Unit = {
      Try(rollbackFunction(lockId))
      kc.deletePathDataIfExists(internalLockPath)
    }

  def error(message: String, cause: Throwable = null): CoordinatorException = {
    CoordinatorException(clazz, path, message, cause)
  }


  def failure[T](message: String, cause: Throwable = null): Try[T] = {
    Failure(
      CoordinatorException(clazz, path, message, cause)
    )
  }

}

  */