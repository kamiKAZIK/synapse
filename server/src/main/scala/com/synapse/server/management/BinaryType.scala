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
    case "jar" => Jar
    case "egg" => Egg
    case _ => throw new InvalidParameterException(s"Binary type '$binaryType' is not supported.")
  }
}
