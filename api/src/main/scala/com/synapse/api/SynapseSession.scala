package com.synapse.api

import org.apache.spark.sql.SparkSession

trait SynapseSession {
  def sparkSession: SparkSession
}
