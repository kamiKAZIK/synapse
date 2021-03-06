package com.synapse.server

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.synapse.server.routing.{BinaryRouter, JobRouter, Router, SessionRouter}
import com.synapse.server.service.{BinaryService, JobService, SessionService}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object SynapseServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("synapse-server")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val config = ConfigFactory.load()

    val binaryService = system.actorOf(Props[BinaryService], "binary-service")
    val sessionService = system.actorOf(Props[SessionService], "session-service")
    val jobService = system.actorOf(Props[JobService], "jobs-service")

    val (host, port) = ("localhost", 33000)
    val bindingFuture = Http().bindAndHandle(
      router(
        new BinaryRouter(binaryService, config),
        new SessionRouter(sessionService),
        new JobRouter(jobService)
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

  private def router(routers: Router*): Route = routers
    .map(_.route)
    .reduceLeft(_ ~ _)
}
