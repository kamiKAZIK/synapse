package com.synapse.manager

import com.synapse.manager.storage.Storage

trait Manager[Key, Value] {
  def storage: Storage[Key, Value]

  def attach(key: Key, value: Value): Value = storage.save(key, value) match {
    case None => value
    case Some(_) => throw new IllegalArgumentException(s"Item with given key '${key}' already exists.")
  }

  def detach(key: Key): Value = storage.remove(key) match {
    case None => throw new IllegalArgumentException(s"Item with given key '${key}' does not exist.")
    case Some(value) => value
  }

  def list: Map[Key, Value] = storage.load

  def retrieve(key: Key): Value = storage.load(key) match {
    case None => throw new IllegalArgumentException(s"Item with given key '${key}' does not exist.")
    case Some(value) => value
  }
}
