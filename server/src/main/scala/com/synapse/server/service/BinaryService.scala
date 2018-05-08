package com.synapse.server.service

import java.util.Base64

import akka.actor.{Actor, ActorLogging}
import com.synapse.server.service.BinaryService.{FoundBinary, SearchBinaries, UploadBinary}

class BinaryService extends Actor with ActorLogging {
  override def receive: Receive = {
    case SearchBinaries(path) => sender ! (
      path match {
        case Some(path) =>
          searchBinaries(path)
        case None =>
          searchBinaries
      }
    )
    case UploadBinary(name, data) =>
      uploadBinary(name, Base64.getDecoder.decode(data))
    case _ => log.error("Invalid Message")
  }

  private def searchBinaries(path: String): List[FoundBinary] = List(
    FoundBinary("key1", "path1")
  )

  private def searchBinaries(): List[FoundBinary] = List(
    FoundBinary("key1", "path1"),
    FoundBinary("key2", "path2")
  )

  private def uploadBinary(name: String, data: Array[Byte]): Unit = {

  }
}

object BinaryService {
  case class SearchBinaries(path: Option[String])

  case class FoundBinary(key: String, path: String)

  case class UploadBinary(name: String, data: String)
}
