package com.synapse.server.persistence.dao

import com.synapse.server.persistence.entity.{Binary, BinaryContent}
import slick.lifted.TableQuery
import slick.jdbc.H2Profile.api._
import java.security.MessageDigest

import scala.concurrent.ExecutionContext.Implicits.global

class BinaryDao(database: Database) {
  /*def searchBinaries(name: Option[String]) =
    database.run(
      name match {
        case Some(name) =>
          query.filter(_.name like name).result
        case None =>
          query.result
      }
    ).map {
      case (name, extension, content) =>
        Binary(name, extension, content)
  }*/

  def saveBinary(binary: Binary, binaryContent: BinaryContent) = {
    database.run(
      Binary.query.filter(_.name === binary.name)
        .filter(_.extension === binary.extension)
        .filter(_.hash === binary.hash).result.headOption.flatMap {
          case None =>
            Binary.query.insertOrUpdate(binary.name, binary.extension, binary.hash)
              .andThen(BinaryContent.query += (binaryContent.hash, binaryContent.uploaded, binaryContent.binary))
          case Some(_) =>
            DBIO.failed(new RuntimeException("Nothing done."))
        }.transactionally
    )
  }


  def deleteBinary(name: String) =
    database.run(Binary.query.filter(_.name === name).delete)

  def revertBinary(name: String) = {
    BinaryContent.query.join(Binary.query.filter(_.name === name)).on(_.hash === _.hash)
  }

  private def hash(bytes: Array[Byte]) =
    MessageDigest.getInstance("SHA-256").digest(bytes)
}
