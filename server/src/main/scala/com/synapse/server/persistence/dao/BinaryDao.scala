package com.synapse.server.persistence.dao

import com.synapse.server.management.BinaryType
import com.synapse.server.persistence.entity.{Binary, BinaryContent}
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BinaryDao(database: Database) {
  def findBinary(name: String, extension: String): Future[Option[Binary]] = database.run(
    Binary.query
      .filter(_.name === name)
      .filter(_.extension === extension)
      .result
      .headOption
      .map(o => o.map {
        case (i, n, e) =>
          Binary(i, n, BinaryType.fromString(e))
      })
  )

  def saveBinary(binaryContent: BinaryContent): Future[Int] = database.run(
    BinaryContent.query
      .filter(_.binaryId === binaryContent.binaryId)
      .filter(_.hash === binaryContent.hash)
      .result
      .headOption
      .flatMap {
        case None =>
          BinaryContent.query += (binaryContent.hash, binaryContent.binaryId, binaryContent.uploaded, binaryContent.binary)
        case Some(_) =>
          DBIO.failed(new IllegalArgumentException("Exact same version of binary is already stored"))
      }.transactionally
  )

  def findLatest(name: String, extension: String): Future[Option[(String, String, Array[Byte])]] = database.run(
    Binary.query
      .filter(_.name === name)
      .filter(_.extension === extension)
      .flatMap(b => BinaryContent.query
        .sortBy(_.uploaded.desc)
        .filter(_.binaryId === b.id)
        .map(bc => (b.name, b.extension, bc.content))
      )
      .result
      .headOption
  )

  def saveBinary(binary: Binary, binaryContent: BinaryContent): Future[Int] = database.run(
    (Binary.query += (binary.id, binary.name, binary.binaryType.extension))
      .andThen(
        BinaryContent.query
          .filter(_.binaryId === binary.id)
          .filter(_.hash === binaryContent.hash)
          .result
          .headOption
          .flatMap {
            case None =>
              BinaryContent.query += (binaryContent.hash, binaryContent.binaryId, binaryContent.uploaded, binaryContent.binary)
            case Some(_) =>
              DBIO.failed(new IllegalArgumentException("Exact same version of binary is already stored"))
          }
      )
      .transactionally
  )

  def deleteBinary(name: String, extension: String): Future[Int] = database.run(
    Binary.query
      .filter(_.name === name)
      .filter(_.extension === extension)
      .delete
      .transactionally
  )

  def revertBinary(name: String, extension: String): Future[Int] = database.run(
    Binary.query
      .filter(_.name === name)
      .filter(_.extension === extension)
      .flatMap(b => BinaryContent.query
        .sortBy(_.uploaded.desc)
        .filter(_.binaryId === b.id)
      )
      .result
      .headOption
      .flatMap {
        case Some((hash, binaryId, _, _)) =>
          BinaryContent.query
            .filter(_.hash === hash)
            .filter(_.binaryId === binaryId)
            .delete
        case None =>
          DBIO.failed(new IllegalArgumentException("No binaries found to revert"))
      }.transactionally
  )
}

