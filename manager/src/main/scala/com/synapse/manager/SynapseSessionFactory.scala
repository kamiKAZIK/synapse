package com.synapse.manager

import com.synapse.api.Session

trait SynapseSessionFactory {
  def create(): Session
}
