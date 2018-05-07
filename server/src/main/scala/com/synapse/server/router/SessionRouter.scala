package com.synapse.server.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object SessionRouter extends Router {
  override def route: Route = path("sessions") {
    get {
      complete("Session List")
    }
  }
}
