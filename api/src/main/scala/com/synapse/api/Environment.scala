package com.synapse.api

trait Environment {
  def jobId: String

  def namedObjects: NamedObjects
}
