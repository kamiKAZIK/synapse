package com.synapse.server.api

import argonaut.Argonaut.jdecode2L

package object request {
  implicit def UploadBinaryJson =
    jdecode2L(UploadBinary.apply)("name", "encoded-binary")
}
