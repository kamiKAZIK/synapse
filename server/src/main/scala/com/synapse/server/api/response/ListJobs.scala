package com.synapse.server.api.response

case class ListJobs(jobs: List[ListJobs.Job])

object ListJobs {
  case class Job(name: String)
}
