package com.synapse.server.service

import java.io.File

import com.synapse.manager.Manager
import com.synapse.server.api.response.SearchBinariesResponse

trait BinaryService {
  def manager: Manager[File]

  def search(path: Option[String]): SearchBinariesResponse = convertStringMap(search(path, manager.list))

  private def search(path: Option[String], container: Map[String, File]): Map[String, String] = path match {
    case None => convertFileMap(container)
    case Some(value) => convertFileMap(container).filter {
      case (_, path) => path.contains(value)
    }
  }

  private def convertFileMap(container: Map[String, File]): Map[String, String] = container.map {
    case (key, file) =>
      key -> file.getAbsolutePath
  }

  private def convertStringMap(container: Map[String, String]): SearchBinariesResponse = SearchBinariesResponse(
    container.map {
      case (key, path) =>
        SearchBinariesResponse.BinaryDescription(key, path)
    }.toList
  )
}
