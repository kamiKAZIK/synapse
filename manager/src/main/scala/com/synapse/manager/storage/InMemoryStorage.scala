package com.synapse.manager.storage

import scala.collection.concurrent.TrieMap

class InMemoryStorage[Key, Value] extends Storage[Key, Value] {
  private val storage: TrieMap[Key, Value] = TrieMap.empty[Key, Value]

  override def save(key: Key, value: Value): Option[Value] = storage.putIfAbsent(key, value)

  override def load(key: Key): Option[Value] = storage.get(key)

  override def load: Map[Key, Value] = storage.toMap

  override def remove(key: Key): Option[Value] = storage.remove(key)
}
