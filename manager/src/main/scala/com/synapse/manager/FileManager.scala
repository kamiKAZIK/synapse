package com.synapse.manager

import java.io.File

class FileManager() extends Manager[File] {
  override def detach(key: String): Unit = detach(key, _.delete())
}
