package com.synapse.core

trait BinaryPackage {
  def extension: String
}

object BinaryPackage {
  case object Jar extends BinaryPackage {
    override val extension = "jar"
  }

  case object Egg extends BinaryPackage {
    override val extension = "egg"
  }

  case class Description(name: String, binaryPackage: BinaryPackage)

  def of(extension: String): BinaryPackage = extension match {
    case "jar" => Jar
    case "egg" => Egg
    case _ => throw new IllegalArgumentException(s"Binary type '$extension' is not supported.")
  }
}
