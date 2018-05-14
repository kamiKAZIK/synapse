package com.synapse.api

trait Job[In, Out] {
  def execute(session: Session, input: In): Out
}
