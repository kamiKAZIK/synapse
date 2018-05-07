package com.synapse.manager.storage

trait Storage[Key, Value] {
  def save(key: Key, value: Value): Option[Value]
  def load(key: Key): Option[Value]
  def load: Map[Key, Value]
  def remove(key: Key): Option[Value]
}
