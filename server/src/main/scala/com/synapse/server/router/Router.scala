package com.synapse.server.router

import akka.http.scaladsl.server.Route

trait Router {
  def route: Route
}
