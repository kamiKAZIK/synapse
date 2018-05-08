package com.synapse.server.api.response

import com.synapse.server.api.response.SearchBinariesResponse.BinaryDescription

case class SearchBinariesResponse(descriptions: List[BinaryDescription])

object SearchBinariesResponse {
  case class BinaryDescription(key: String,
                               path: String)
}
