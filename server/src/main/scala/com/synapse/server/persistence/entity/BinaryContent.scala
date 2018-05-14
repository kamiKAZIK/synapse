package com.synapse.server.persistence.entity

import java.sql.{Blob, Timestamp}

import slick.jdbc.H2Profile.api._

case class BinaryContent(hash: Array[Byte], uploaded: Timestamp, binary: Blob)

object BinaryContent {
  val query = TableQuery[BinaryContent.Definition]

  class Definition(tag: Tag) extends Table[(Array[Byte], Timestamp, Blob)](tag, "BINARY_CONTENTS") {
    def hash = column[Array[Byte]]("HASH", O.PrimaryKey)
    def uploaded = column[Timestamp]("UPLOADED")
    def content = column[Blob]("CONTENT")
    def * = (hash, uploaded, content)

    def binary = foreignKey("BINARY_FK", hash, Binary.query)(_.hash, onDelete = ForeignKeyAction.Cascade)
  }
}