package com.synapse.server.routing

import akka.actor.ActorRef
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.synapse.server.api.response.ListJobs
import com.synapse.server.service.JobService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class JobRouter(private val service: ActorRef) extends Router {
  implicit val timeout = Timeout(10 seconds)

  override def route = path("jobs") {
    get {
      encodeResponseWith(Gzip) {
        parameters('name.?) { name =>
          complete {
            searchJobs(name)
          }
        }
      }
    }
  }

  private def searchJobs(name: Option[String]) =
    (service ? JobService.SearchJobs(name))
      .mapTo[List[JobService.FoundJob]]
      .map(jobs => ListJobs(
        jobs.map(job => ListJobs.Job(
          job.name
        ))
      ))
}
