package com.synapse.server

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.synapse.server.router.{BinaryRouter, JobRouter, Router, SessionRouter}
import com.synapse.server.service.{BinaryService, JobService, SessionService}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object SynapseServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("synapse-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val binaryService = system.actorOf(Props[BinaryService], "binaryService")
    val sessionService = system.actorOf(Props[SessionService], "sessionService")
    val jobService = system.actorOf(Props[JobService], "jobService")

    val (host, port) = ("localhost", 33000)
    val bindingFuture = Http().bindAndHandle(
      router(
        new BinaryRouter(binaryService),
        new SessionRouter(sessionService),
        new JobRouter()
      ),
      host,
      port
    )

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  private def router(routers: Router*): Route = routers.reduceLeft(_.route ~ _.route)
}
