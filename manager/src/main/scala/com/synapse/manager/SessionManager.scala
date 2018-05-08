package com.synapse.manager

import com.synapse.api.SynapseSession

object SessionManager extends Manager[SynapseSession] {
  override def detach(key: String): Unit = detach(key, _.stop)
}
