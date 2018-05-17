package com.synapse.server.persistence.entity

import java.util.UUID

import com.synapse.server.management.BinaryType
import slick.jdbc.H2Profile.api._
import slick.lifted.{ProvenShape, TableQuery}

case class Binary(id: UUID, name: String, binaryType: BinaryType)

object Binary {
  val query = TableQuery[Binary.Definition]

  class Definition(tag: Tag) extends Table[(UUID, String, String)](tag, "BINARIES") {
    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)
    def name: Rep[String] = column[String]("NAME")
    def extension: Rep[String] = column[String]("EXTENSION")
    def * : ProvenShape[(UUID, String, String)] = (id, name, extension)
  }
}
