package com.synapse.api

import org.apache.spark.sql.SparkSession

trait Session {
  def spark: SparkSession
}
