package com.synapse.server.api.response

import com.synapse.server.api.response.SearchJobsResponse.JobDescription

case class SearchJobsResponse(descriptions: List[JobDescription])

object SearchJobsResponse {
  case class JobDescription(key: String)
}
