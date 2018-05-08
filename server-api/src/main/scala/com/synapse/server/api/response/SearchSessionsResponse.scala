package com.synapse.server.api.response

import com.synapse.server.api.response.SearchSessionsResponse.SessionDescription

case class SearchSessionsResponse(descriptions: List[SessionDescription])

object SearchSessionsResponse {
  case class SessionDescription(key: String)
}
