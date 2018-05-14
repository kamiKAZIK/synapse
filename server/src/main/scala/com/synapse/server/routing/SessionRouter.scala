package com.synapse.server.routing

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.synapse.server.api.response.ListSessions
import com.synapse.server.service.SessionService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SessionRouter(private val service: ActorRef) extends Router {
  implicit val timeout = Timeout(10 seconds)

  override def route = path("sessions") {
    get {
      parameters('name.?) { name =>
        complete {
          searchSessions(name)
        }
      }
    }
  }

  private def searchSessions(name: Option[String]) =
    (service ? SessionService.SearchSessions(name))
      .mapTo[List[SessionService.FoundSession]]
      .map(sessions => ListSessions(
        sessions.map(session => ListSessions.Session(
          session.name
        ))
      ))
}
