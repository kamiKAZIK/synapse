package com.synapse.server.router

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import com.synapse.server.api.request.UploadBinaryRequest
import com.synapse.server.api.response.SearchBinariesResponse
import com.synapse.server.router.binary.request._
import com.synapse.server.router.binary.response._
import com.synapse.server.service.BinaryService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

class BinaryRouter(private val service: ActorRef) extends Router {
  override def route: Route = path("binares") {
    get {
      parameters('path.?) { path =>
        complete {
          (service ? BinaryService.SearchBinaries(path))
            .mapTo[List[BinaryService.FoundBinary]]
            .map(binaryList => SearchBinariesResponse(
              binaryList.map(binary => SearchBinariesResponse.BinaryDescription(
                binary.key,
                binary.path
              ))
            ))
        }
      }
    }
  } ~ path("binary") {
    post {
      entity(as[UploadBinaryRequest]) { request =>
        complete {
          service ! BinaryService.UploadBinary(request.name, request.encodedBinary)
        }
      }
    }
  }
}
