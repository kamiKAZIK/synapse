package com.synapse.server.persistence.entity

import java.sql.Timestamp
import java.util.UUID

import slick.jdbc.H2Profile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape, TableQuery}

case class BinaryContent(hash: Array[Byte], binaryId: UUID, uploaded: Timestamp, binary: Array[Byte])

object BinaryContent {
  val query = TableQuery[BinaryContent.Definition]

  class Definition(tag: Tag) extends Table[(Array[Byte], UUID, Timestamp, Array[Byte])](tag, "BINARY_CONTENTS") {
    def hash: Rep[Array[Byte]] = column[Array[Byte]]("HASH")
    def binaryId: Rep[UUID] = column[UUID]("BINARY_ID")
    def uploaded: Rep[Timestamp] = column[Timestamp]("UPLOADED")
    def content: Rep[Array[Byte]] = column[Array[Byte]]("CONTENT")
    def * : ProvenShape[(Array[Byte], UUID, Timestamp, Array[Byte])] = (hash, binaryId, uploaded, content)

    def pk= primaryKey("BC_PK", (hash, binaryId))

    def binary: ForeignKeyQuery[Binary.Definition, (UUID, String, String)] =
      foreignKey("BINARY_FK", binaryId, Binary.query)(_.id, onDelete = ForeignKeyAction.Cascade)
  }
}