package com.synapse.server.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object JobRouter extends Router {
  override def route: Route = path("jobs") {
    get {
      complete("Job List")
    }
  }
}
