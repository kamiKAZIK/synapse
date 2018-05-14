package com.synapse.core

import com.synapse.api.Session

import scala.collection.concurrent.TrieMap

class SessionManager {
  private val container = TrieMap.empty[String, Session]

  def add(name: String, session: Session): Unit = container.putIfAbsent(name, session)

  def remove(name: String): Unit = container.remove(name)
}
