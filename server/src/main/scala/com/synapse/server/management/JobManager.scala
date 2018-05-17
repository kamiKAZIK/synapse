package com.synapse.server.management

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorLogging}
import com.synapse.server.management.JobManager.StartJob
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

trait StatusMessage {
  val jobId: String
}

object JobManager {
  case class StartJob(name: String, master: String, classPath: String, config: Config)

  case class JobStarted(override val jobId: String) extends StatusMessage
}

class JobManager extends Actor with ActorLogging {
  private[this] val runningJobs = new AtomicInteger(0)

  override def receive: Receive = {
    case StartJob(name, master, classPath, config) =>
      val sessionBuilder = SparkSession.builder()
        .appName(name)
        .master(master)
      Try(sessionBuilder.enableHiveSupport()) match {
        case Failure(t) =>
          log.warning(s"Hive support not enabled - ${t.getMessage}")
      }

      sessionBuilder.getOrCreate()
  }
}