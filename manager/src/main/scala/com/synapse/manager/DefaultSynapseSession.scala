package com.synapse.manager

import com.synapse.api.Session
import org.apache.spark.sql.SparkSession

class DefaultSynapseSession(override val sparkSession: SparkSession) extends Session {
}
