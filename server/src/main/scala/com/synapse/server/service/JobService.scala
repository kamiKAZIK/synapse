package com.synapse.server.service

import akka.actor.{Actor, ActorLogging}
import com.synapse.server.service.JobService.{FoundJob, SearchJobs}

class JobService extends Actor with ActorLogging {
  override def receive: Receive = {
    case SearchJobs(key) => sender ! (
      key match {
        case Some(key) =>
          searchJobs(key)
        case None =>
          searchJobs
      }
    )
  }

  private def searchJobs(key: String): List[FoundJob] = List(
    FoundJob("key1")
  )

  private def searchJobs(): List[FoundJob] = List(
    FoundJob("key1"),
    FoundJob("key2")
  )
}

object JobService {
  case class SearchJobs(key: Option[String])

  case class FoundJob(name: String)
}
