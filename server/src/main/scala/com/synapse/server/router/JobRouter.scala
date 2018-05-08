package com.synapse.server.router

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import com.synapse.server.api.response.SearchJobsResponse
import com.synapse.server.router.job.response._
import com.synapse.server.service.JobService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

class JobRouter(private val service: ActorRef) extends Router {
  override def route: Route = path("jobs") {
    get {
      parameters('key.?) { key =>
        complete {
          (service ? JobService.SearchJobs(key))
            .mapTo[List[JobService.FoundJob]]
            .map(jobList => SearchJobsResponse(
              jobList.map(job => SearchJobsResponse.JobDescription(
                job.key
              ))
            ))
        }
      }
    }
  }
}
