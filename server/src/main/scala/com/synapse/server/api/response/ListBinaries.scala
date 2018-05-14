package com.synapse.server.api.response

case class ListBinaries(binaries: List[ListBinaries.Binary])

object ListBinaries {
  case class Binary(name: String,
                    path: String)
}
