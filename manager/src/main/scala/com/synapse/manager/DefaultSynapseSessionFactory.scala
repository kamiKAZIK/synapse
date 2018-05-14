package com.synapse.manager

import com.synapse.api.Session
import org.apache.spark.sql.SparkSession

class DefaultSynapseSessionFactory extends SynapseSessionFactory {
  override def create(name: String): Session = new DefaultSynapseSession(SparkSession.builder().appName(name).)
}
