package com.synapse.manager

import com.synapse.api.SynapseSession

trait SynapseSessionFactory {
  def create(): SynapseSession
}
