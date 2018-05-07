package com.synapse.manager

import com.synapse.api.Session
import org.apache.spark.sql.SparkSession

class SessionImpl(private val config: String) extends Session {
  override val sparkSession = SparkSession.builder()
    .getOrCreate()

  override def stop: Unit = sparkSession.stop()
}
