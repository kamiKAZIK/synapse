package com.synapse.api

trait Job[JobInput, JobOutput] {
  def execute(session: SynapseSession, input: JobInput): JobOutput
}
