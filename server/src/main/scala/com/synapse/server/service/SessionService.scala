package com.synapse.server.service

import akka.actor.{Actor, ActorLogging}
import com.synapse.server.service.SessionService.{FoundSession, SearchSessions}

class SessionService extends Actor with ActorLogging {
  override def receive: Receive = {
    case SearchSessions(key) => sender ! (
      key match {
        case Some(key) =>
          searchSessions(key)
        case None =>
          searchSessions
      }
    )
    case _ => log.error("Invalid Message")
  }

  private def searchSessions(key: String): List[FoundSession] = List(
    FoundSession("key1")
  )

  private def searchSessions(): List[FoundSession] = List(
    FoundSession("key1"),
    FoundSession("key2")
  )
}

object SessionService {
  case class SearchSessions(key: Option[String])

  case class FoundSession(name: String)
}
