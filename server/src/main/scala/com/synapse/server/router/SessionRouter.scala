package com.synapse.server.router

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import com.synapse.server.api.response.SearchSessionsResponse
import com.synapse.server.router.session.response._
import com.synapse.server.service.SessionService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

class SessionRouter(private val service: ActorRef) extends Router {
  override def route: Route = path("sessions") {
    get {
      parameters('key.?) { key =>
        complete {
          (service ? SessionService.SearchSessions(key))
            .mapTo[List[SessionService.FoundSession]]
            .map(sessionList => SearchSessionsResponse(
              sessionList.map(session => SearchSessionsResponse.SessionDescription(
                session.key
              ))
            ))
        }
      }
    }
  }
}
