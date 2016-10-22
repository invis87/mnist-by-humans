package com.pronvis.mnist_by_humans

import com.typesafe.scalalogging.LazyLogging
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.{Server, ServerApp}
import org.http4s.server.blaze._

import scalaz.concurrent.Task

object Main extends ServerApp with LazyLogging {

  override def server(args: List[String]): Task[Server] = {
    logger.debug("Starting Blaze web server")

    val helloWorldService = HttpService {
      case GET -> Root / "hello" / name =>
        Ok(s"Hello, $name.")
    }

    val builder = BlazeBuilder.bindHttp(8080, "localhost").mountService(helloWorldService, "/api")
    builder.start
  }
}
