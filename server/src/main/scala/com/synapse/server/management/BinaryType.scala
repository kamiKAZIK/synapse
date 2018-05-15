package com.synapse.server.management

import java.security.InvalidParameterException

trait BinaryType {
  def extension: String
}

object BinaryType {
  case object Jar extends BinaryType {
    val extension = "jar"
  }

  case object Egg extends BinaryType {
    val extension = "egg"
  }

  def fromString(binaryType: String): BinaryType = binaryType match {
    case Jar.extension => Jar
    case Egg.extension => Egg
    case _ => throw new InvalidParameterException(s"Binary type '$binaryType' is not supported.")
  }

  def supportedTypes: List[String] = List(Jar.extension, Egg.extension)
}
