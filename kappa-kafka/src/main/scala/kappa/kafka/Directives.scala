package kappa.kafka

import java.util.Properties

import kafka.admin.{AdminUtils, ConsumerGroupCommand, RackAwareMode}
import kafka.utils.ZkUtils
import kappa.Directive._
import kappa._
import org.apache.kafka.common.errors.{TopicExistsException, UnknownTopicOrPartitionException}


/**
  * Created by j0rd1 on 8/11/16.
  */
object Directives {

  import Conf._

  private def zkUtils(session: KappaSession): ZkUtils = {
    val zkUrl: String = session.conf.zkUrl
    val sessionTimeout = session.conf(`kappa.kafka.session.timeout`)
    val connectionTimeout = session.conf(`kappa.kafka.connection.timeout`)
    session.getOrCreate(classOf[ZkUtils]) {
      ZkUtils(zkUrl, sessionTimeout, connectionTimeout, isZkSecurityEnabled = false)
    }
  }

  def createKafkaTopic(topic: String, partitions: Int, replication: Int): Directive0 = {
    createKafkaTopic(topic, partitions, replication, new Properties())
  }

  def createKafkaTopic(topic: String, partitions: Int, replication: Int, conf: Properties) =
      Directive0(s"createKafkaTopic($topic)") { session =>
    AdminUtils.createTopic(zkUtils(session), topic, partitions, replication, conf, RackAwareMode.Enforced)
  }

  def createKafkaTopicIfNotExists(topic: String, partitions: Int, replication: Int): Directive0 = {
    createKafkaTopicIfNotExists(topic, partitions, replication, new Properties())
  }

  def createKafkaTopicIfNotExists(topic: String, partitions: Int, replication: Int, conf: Properties) =
      Directive0(s"createKafkaTopicIfNotExists($topic)") { session =>
    try {
      AdminUtils.createTopic(zkUtils(session), topic, partitions, replication, conf, RackAwareMode.Enforced)
    } catch {
      case e: TopicExistsException =>
        session.debug(e.getLocalizedMessage)
    }
  }

  def deleteKafkaTopic(topic: String) = Directive0(s"deleteKafkaTopic($topic)") { session =>
    AdminUtils.deleteTopic(zkUtils(session), topic)
  }

  def deleteKafkaTopicIfExists(topic: String) = Directive0(s"deleteKafkaTopicIfExists($topic)") { session =>
    try {
      AdminUtils.deleteTopic(zkUtils(session), topic)
    } catch {
      case e: /* FIXME TopicAlreadyMarkedForDeletionException |*/ UnknownTopicOrPartitionException =>
    }
  }

  def retrieveKafkaOffsets(topic: String) =
      Directive0(s"retrieveKafkaOffsets($topic)") { session =>
    ConsumerGroupCommand
  }
}
