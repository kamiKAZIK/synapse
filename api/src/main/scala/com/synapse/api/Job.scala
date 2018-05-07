package com.synapse.api

trait Job[JobInput, JobOutput] {
  def execute(session: Session, input: JobInput): JobOutput
}
