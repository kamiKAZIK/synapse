package com.synapse.server

import argonaut.Argonaut._
import argonaut._
import com.synapse.server.api.request.UploadBinaryRequest
import com.synapse.server.api.response.{SearchBinariesResponse, SearchJobsResponse, SearchSessionsResponse}

package object router {
  object binary {
    object response {
      implicit def BinaryDescriptionJson: EncodeJson[SearchBinariesResponse.BinaryDescription] =
        jencode2L((bd: SearchBinariesResponse.BinaryDescription) => (bd.key, bd.path))("key", "path")

      implicit def SearchBinariesResponseJson: EncodeJson[SearchBinariesResponse] =
        jencode1L(SearchBinariesResponse.unapply)("descriptions")
    }

    object request {
      implicit def UploadBinaryRequestJson: DecodeJson[UploadBinaryRequest] =
        jdecode2L(UploadBinaryRequest.apply)("name", "encoded-binary")
    }
  }

  object session {
    object response {
      implicit def SessionDescriptionJson: EncodeJson[SearchSessionsResponse.SessionDescription] =
        jencode1L((sd: SearchSessionsResponse.SessionDescription) => (sd.key))("key")

      implicit def SearchSessionsResponseJson: EncodeJson[SearchSessionsResponse] =
        jencode1L(SearchSessionsResponse.unapply)("descriptions")
    }
  }

  object job {
    object response {
      implicit def JobDescriptionJson: EncodeJson[SearchJobsResponse.JobDescription] =
        jencode1L((jd: SearchJobsResponse.JobDescription) => (jd.key))("key")

      implicit def SearchJobsResponseJson: EncodeJson[SearchJobsResponse] =
        jencode1L(SearchJobsResponse.unapply)("descriptions")
    }
  }
}
