package com.synapse.server.management

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.time.LocalDateTime

import com.typesafe.scalalogging.Logger

class BinaryFileCache(directory: Path) {
  private[this] val logger = Logger[BinaryFileCache]
  private[this] val fileNameRegex = "(.*)-(.*).[0-9a-zA-Z]+".r

  if (!Files.exists(directory)) {
    Files.createDirectories(directory)
  }

  def cache(name: String, uploaded: LocalDateTime,  binaryType: BinaryType, content: Array[Byte]): Path = {
    val fileName = s"$name-$uploaded.${binaryType.extension}"
    val temporaryFile = File.createTempFile(fileName, ".tmp", directory.toFile)
    val temporaryFilePath = temporaryFile.toPath
    val output = new BufferedOutputStream(new FileOutputStream(temporaryFile))

    try {
      logger.debug("Writing {} bytes to a temporary file {}", content.length, temporaryFilePath)

      output.write(content)
      output.flush()
    } finally {
      output.close()
    }

    logger.debug("Renaming the temporary file {} to the target binary name {}", temporaryFilePath, fileName)

    Files.move(temporaryFilePath, temporaryFilePath.resolveSibling(fileName), StandardCopyOption.REPLACE_EXISTING)
  }
//^test-1-\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.[jar|egg]
  def clean(name: String): Unit = directory
    .toFile
    .listFiles()
    .filter(f => fileNameRegex.)
    .foreach(delete(_))

  def purge: Unit = purge(directory)

  private[this] def purge(directory: Path): Unit = directory
    .toFile
    .listFiles
    .foreach(f =>
      if (f.isDirectory) {
        logger.debug("Path {} is a directory, it's files will be purged as well", f.toPath)

        purge(f.toPath)
      } else {
        delete(f)
      })

  private[this] def delete(file: File): Unit = {
    logger.debug("Deleting file {}", file.toPath)

    if (!file.delete()) {
      logger.debug("Failed to delete {}", file.toPath)
    }
  }
}

object BinaryFileCache {
  def main(args: Array[String]): Unit = {
    val cache = new BinaryFileCache(Paths.get("/tmp/tesing"))

    cache.cache("test", LocalDateTime.now(), BinaryType.Jar, Array(1, 2, 3))
    cache.cache("test-1", LocalDateTime.now(), BinaryType.Jar, Array(1, 2, 3))
    cache.clean("test")
    //cache.purge
  }
}
