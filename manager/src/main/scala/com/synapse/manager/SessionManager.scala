package com.synapse.manager

import com.synapse.api.Session

object SessionManager extends Manager[Session] {
  override def detach(key: String): Unit = detach(key, _.stop)
}
