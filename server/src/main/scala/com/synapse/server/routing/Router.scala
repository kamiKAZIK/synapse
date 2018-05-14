package com.synapse.server.routing

import akka.http.scaladsl.server.Route

trait Router {
  def route: Route
}
