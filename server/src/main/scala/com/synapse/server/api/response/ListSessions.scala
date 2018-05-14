package com.synapse.server.api.response

case class ListSessions(sessions: List[ListSessions.Session])

object ListSessions {
  case class Session(name: String)
}
