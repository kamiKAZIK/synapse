package com.synapse.server.routing

import akka.actor.ActorRef
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import akka.pattern.ask
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.{ByteString, Timeout}
import com.synapse.server.api.request.UploadBinary
import com.synapse.server.api.response.ListBinaries
import com.synapse.server.service.BinaryService
import com.typesafe.config.Config
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BinaryRouter(private val service: ActorRef, config: Config) extends Router {
  override def route: Route = path("binares") {
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
      fileUpload("binary") {
        case (metadata, byteSource) =>
          complete {
            uploadBinary(metadata, byteSource)
          }
      }
    }
  }

  private[this] def searchBinaries(name: Option[String]): Future[ListBinaries] =
    (service ? BinaryService.SearchBinaries(name))
      .mapTo[List[BinaryService.FoundBinary]]
      .map(binaries => ListBinaries(
        binaries.map(binary => ListBinaries.Binary(
          binary.name,
          binary.path
        ))
      ))

  private def uploadBinary(metadata: FileInfo, source: Source[ByteString, Any]): Unit =
    null//service ! BinaryService.UploadBinary(request.name, request.encodedBinary)

  private[this] def getTimeout: Long = if (config.hasPath("timeout")) {
    config.getLong("timeout")
  } else {
    30000L
  }

  private[this] implicit def timeout: Timeout = Timeout(getTimeout milliseconds)
}
