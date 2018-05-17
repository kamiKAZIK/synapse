package com.synapse.api

trait Job[In, Out]  extends Serializable {
  def execute(session: Session, input: In): Out
}
