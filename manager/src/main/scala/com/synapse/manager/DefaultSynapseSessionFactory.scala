package com.synapse.manager

import com.synapse.api.SynapseSession
import org.apache.spark.sql.SparkSession

class DefaultSynapseSessionFactory extends SynapseSessionFactory {
  override def create(name: String): SynapseSession = new DefaultSynapseSession(SparkSession.builder().appName(name).)
}
