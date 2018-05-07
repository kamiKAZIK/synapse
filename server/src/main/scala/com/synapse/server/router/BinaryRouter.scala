package com.synapse.server.router

import java.io.File

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.synapse.manager.Manager
import com.synapse.server.api.response.SearchBinariesResponse

class BinaryRouter(private val manager: Manager[File]) extends Router {
  override def route: Route = path("binares") {
    get {
      parameters('path.?) { pathParameter =>
        complete {
          val files = manager.list.map {
            case (key, file) =>
              key -> file.getAbsolutePath
          }
          val res = pathParameter match {
            case None => files
            case Some(value) => files.filter {
              case (_, path) =>
                path.contains(value)
            }
          }
          SearchBinariesResponse(
            res.map {
              case (key, path) =>
                SearchBinariesResponse.BinaryDescription(key, path)
            }.toList
          )
          ""
        }
      }
    }
  } ~ path("binary") {
    post {
      complete("Binary Upload")
    }
  }
}
