package com.synapse.server.management

import java.nio.file.Paths
import java.security.MessageDigest
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import com.synapse.server.persistence.dao.BinaryDao
import com.synapse.server.persistence.entity.{Binary, BinaryContent}
import com.typesafe.config.Config
import javax.xml.crypto.dsig.DigestMethod
import slick.jdbc.H2Profile.api._

class BinaryManager(config: Config) {
  private val binaryCache = new BinaryFileCache(Paths.get(config.getString("synapse.cache.directory")))
  private val binaryDao = new BinaryDao(Database.forURL(
    config.getString("synapse.jdbc.url"),
    config.getString("synapse.jdbc.user"),
    config.getString("synapse.jdbc.password")
  ))

  /*def store(name: String, binaryType: BinaryType, content: Array[Byte]) = {
    binaryDao.findBinary(name, binaryType.extension)
      .flatMap {
        case Some(bin) =>
          binaryDao.saveBinary(BinaryContent(hash(content), bin.id, Timestamp.valueOf(LocalDateTime.now()), content))
        case None =>
          val id = UUID.randomUUID()
          binaryDao.saveBinary(
            Binary(id, name, binaryType),
            BinaryContent(hash(content), id, Timestamp.valueOf(LocalDateTime.now()), content)
          )
    }.map(_ => )
  }*/

  private[this] def hash(bytes: Array[Byte]): Array[Byte] = MessageDigest
    .getInstance(DigestMethod.SHA256)
    .digest(bytes)
}
