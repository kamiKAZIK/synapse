package com.synapse.server.management

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Path, StandardCopyOption}
import java.util.{Base64, Comparator}

import com.typesafe.scalalogging.Logger

class BinaryFileCache(directory: Path) {
  private[this] val logger = Logger[BinaryFileCache]

  if (!Files.exists(directory)) {
    Files.createDirectories(directory)
  }

  def retrieve(name: String, hash: Array[Byte], binaryType: BinaryType): Option[Path] =
    Some(directory.resolve(name).resolve(fileName(name, encode(hash), binaryType)))
      .filter(Files.exists(_))

  def cache(name: String, hash: Array[Byte], binaryType: BinaryType, content: Array[Byte]): Path = {
    val cachedPath = directory.resolve(name)
    if (!Files.exists(cachedPath)) {
      Files.createDirectories(cachedPath)
    }
    val binaryName = fileName(name, encode(hash), binaryType)
    val temporaryFile = File.createTempFile(binaryName, ".tmp", cachedPath.toFile)
    val temporaryFilePath = temporaryFile.toPath
    val output = new BufferedOutputStream(new FileOutputStream(temporaryFile))

    try {
      logger.debug("Writing {} bytes to a temporary file {}", content.length, temporaryFilePath)

      output.write(content)
      output.flush()
    } finally {
      output.close()
    }

    logger.debug("Renaming the temporary file {} to the target binary name {}", temporaryFilePath, binaryName)

    Files.move(temporaryFilePath, temporaryFilePath.resolveSibling(binaryName), StandardCopyOption.REPLACE_EXISTING)
  }

  def purge(name: String): Unit = purge(directory.resolve(name))

  def purge(): Unit = purge(directory)

  private[this] def purge(directory: Path): Unit = Files
    .walk(directory)
    .sorted(Comparator.reverseOrder())
    .map[File](_.toFile)
    .forEach(delete(_))

  private[this] def delete(file: File): Unit = {
    logger.debug("Deleting file {}", file.toPath)

    if (!file.delete()) {
      logger.debug("Failed to delete {}", file.toPath)
    }
  }

  private[this] def fileName(name: String, hash: String, binaryType: BinaryType): String =
    s"$name-$hash.${binaryType.extension}"

  private[this] def encode(hash: Array[Byte]): String = Base64.getEncoder.encodeToString(hash)
}
