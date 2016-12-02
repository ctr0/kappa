package kappa

import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.{ExponentialBackoffRetry, RetryOneTime}
import org.apache.zookeeper.KeeperException
import org.apache.zookeeper.data.Stat

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * Created by j0rd1 on 4/11/16.
  */
class KappaSession(val conf: KappaConf) extends Logging {


  private val data = mutable.Map[String, Any]()

  def getOrCreate[T](clazz: Class[T])(instance: T): T = {
    getOrCreate(clazz.getCanonicalName)(instance)
  }

  def getOrCreate[T](key: String)(instance: T): T = {
    data.synchronized {
      data.getOrElseUpdate(key, instance).asInstanceOf[T]
    }
  }

  def getOrCreate[T](clazz: Class[T], key: String)(instance: T): T = {
    getOrCreate(s"${clazz.getCanonicalName}@$key")(instance)
  }

  def checkExistsPath(path: String): Stat = {
    zkClient.checkExists.forPath(path)
  }

  def checkExistsPaths(paths: Seq[String]): Try[Seq[(String, Stat)]] = {
    /*Try {
      zkClient.transaction().forOperations {
        paths.map {
          zkClient.transactionOp().check().forPath
        }.asJava
      }.asScala.zip(paths).flatMap { case (result, path) =>
        val stat = result.getResultStat
        if (stat != null) Some((path, stat)) else None
      }
    }*/
    null
  }

  @throws[Exception]
  def createPathIfNotExists(path: String): Unit = {
    try {
      zkClient.create.creatingParentContainersIfNeeded.forPath(path)
    } catch {
      case ignore: KeeperException.NodeExistsException =>
    }
  }

  def createPathAndDataIfNotExists(path: String, rawData: RawData): Unit = {
    // FIXME data
    try {
      zkClient.create.creatingParentContainersIfNeeded.forPath(path)
    } catch {
      case ignore: KeeperException.NodeExistsException =>
    }
  }

  import scala.collection.JavaConverters._

  def getPathChildren(path: String): Seq[String] = {
    zkClient.getChildren.forPath(path).asScala
  }


  def deletePathDataIfExists(path: String): Unit = ???

  def deletePathAndDataIfExists(path: String): Unit = ???

  /**
    * throws exception if data not write
    */
  def setOrCreatePathData(path: String, data: String) = ???


  /** /kappa/{instance_name} */
  private[kappa] val zkBasePath = "/kappa/instance_name"

  private[kappa] val zkClient = {
    //val retryPolicy = new ExponentialBackoffRetry(1000, 3)
    val retryPolicy = new RetryOneTime(100)
    val connStr = conf.zkUrl.replace("zk://", "")
    val curatorBuilder = CuratorFrameworkFactory.builder()
      .connectString(connStr)
      .retryPolicy(retryPolicy)

    conf.zkAuthorization match {
      case Some((scheme, auth)) => curatorBuilder.authorization(scheme, auth.getBytes)
      case None =>
    }

    val client = curatorBuilder.build()
    client.start()
    client
  }

  def getPathDataIfExists(path: String): Option[RawData] = {
    Try {
      zkClient.getData.forPath("")
    } match {
      case Success(bytes) => Some(RawData(bytes))
      case Failure(error) =>
        // Fixme debug log
        None
    }
  }

}