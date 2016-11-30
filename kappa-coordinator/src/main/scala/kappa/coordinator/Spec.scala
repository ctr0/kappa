package kappa.coordinator

import kappa._

import scala.collection.mutable

/**
  * Created by j0rd1 on 2/11/16.
  */
abstract class Spec extends KappaJob {

  // FIXME move to session
  private def locks = mutable.Map[Path, Lock]()

  private def lock(path: Path): Lock = {
    session.getOrCreate(classOf[Lock], path) {
      new Lock(session, path)
    }
  }

  def writeLock(): Directive1[WriteLock] = lock(domain).writeLock()

  def writeLock(path: Path): Directive1[WriteLock] = lock(domain/path).writeLock()

  def readLock(): Directive1[ReadLock] = lock(domain).readLock()

  def readLock(path: Path): Directive1[ReadLock] = lock(domain/path).readLock()


/*
  protected def lock(node: String, dependsOn: String*)(f: LockId => Directive)
      (implicit coordinator: Coordinator): Lock = new ProcessLock(getClass, Path(path, node), f)

  protected def lockStatus(id: String): Seq[LockId] = {
    session.getPathChildren(path).map { child =>
      val path = ZKPaths.makePath(child, ProcessLock.LOCK_ZNODE_NAME)
      session.getPathDataIfExists(path) match {
        case Some(data) =>
          LockId.parse(data.asUtf8String) match {
            case Success(procId) => procId
            case Failure(error) => LockId(child, Instant.EPOCH) // should not reach
          }
        case None => LockId(child, Instant.EPOCH) // should not reach
      }
    }
  }

  def complete()(implicit kc: KappaContext): Unit = {

    kc.getPathChildren(path).filter(_.startsWith("_kappa_")).foreach { child =>
      kc.getPathDataIfExists(child) match {
        case Some(data) =>
          ProcId.parse(data.asUtf8String) match {
            case Success(procId) => TransactionalLock.
          }
        case None =>
      }
    }

    locks.reverse.foreach { lock =>
      try {
        lock.rollbackAndResetLock(Some(lock.procId))
      } catch {
        case t: Throwable => // TODO log any error here (fatal errors too)
      }
    }
  }

  def complete(): Unit = {
    locks.reverse.foreach { lock =>
      try {
        lock.rollbackAndResetLock(Some(lock.procId))
      } catch {
        case t: Throwable => // TODO log any error here (fatal errors too)
      }
    }
  }
*/

}
