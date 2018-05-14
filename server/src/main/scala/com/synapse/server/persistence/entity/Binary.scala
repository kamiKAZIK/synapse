package com.synapse.server.persistence.entity

import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

case class Binary(name: String, extension: String, hash: Array[Byte])

object Binary {
  val query = TableQuery[Binary.Definition]

  class Definition(tag: Tag) extends Table[(String, String, Array[Byte])](tag, "BINARIES") {
    def name = column[String]("NAME", O.PrimaryKey)
    def extension = column[String]("EXTENSION")
    def hash = column[Array[Byte]]("HASH")
    def * = (name, extension, hash)
  }
}
