package com.synapse.server.routing

import akka.actor.ActorRef
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.synapse.server.api.request.UploadBinary
import com.synapse.server.api.response.ListBinaries
import com.synapse.server.service.BinaryService
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class BinaryRouter(private val service: ActorRef) extends Router {
  implicit val timeout = Timeout(10 seconds)

  override def route = path("binares") {
    get {
      encodeResponseWith(Gzip) {
        parameters('name.?) { name =>
          complete {
            searchBinaries(name)
          }
        }
      }
    }
  } ~ path("binary") {
    post {
      entity(as[UploadBinary]) { request =>
        complete {
          uploadBinary(request)
        }
      }
    }
  }

  private def searchBinaries(name: Option[String]) =
    (service ? BinaryService.SearchBinaries(name))
      .mapTo[List[BinaryService.FoundBinary]]
      .map(binaries => ListBinaries(
        binaries.map(binary => ListBinaries.Binary(
          binary.name,
          binary.path
        ))
      ))

  private def uploadBinary(request: UploadBinary) =
    service ! BinaryService.UploadBinary(request.name, request.encodedBinary)
}
