package com.synapse.api

/**
  * implementations of this trait should handle the specifics
  * of each named object's persistence
  */
trait NamedObjectPersistence[O <: NamedObject] {
  /**
    * persists an object with the given name
    * @param namedObject - object to be persisted
    * @param name - name of the object
    */
  def persist(namedObject: O, name: String): Unit

  /**
    * update reference to named object so that it does not get GCed
    * @param namedObject - reference to this object is to be refreshed
    */
  def refresh(namedObject: O): O

  /**
    * unpersist the given object
    * @param namedObject - object to be unpersisted
    */
  def unpersist(namedObject: O): Unit
}
