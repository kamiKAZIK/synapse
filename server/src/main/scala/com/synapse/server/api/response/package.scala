package com.synapse.server.api

import argonaut.Argonaut.jencode1L
import argonaut.EncodeJson

package object response {
  implicit def ListBinariesBinaryJson =
    jencode1L((b: ListBinaries.Binary) => (b.name))("name")

  implicit def ListBinariesJson =
    jencode1L(ListBinaries.unapply)("descriptions")

  implicit def ListSessionsSessionJson =
    jencode1L((s: ListSessions.Session) => (s.name))("name")

  implicit def ListSessionsJson =
    jencode1L(ListSessions.unapply)("sessions")

  implicit def ListJobsJobJson =
    jencode1L((j: ListJobs.Job) => (j.name))("name")

  implicit def ListJobsJson =
    jencode1L(ListJobs.unapply)("jobs")
}
