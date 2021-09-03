package com.eskimi.bidservice

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.util.{Failure, Success}

object BidServiceHttpServer {

  def start(routes: Route)(implicit system: ActorSystem[_]): Unit = {

    val interface = system.settings.config.getString("bid-service.interface")
    val port = system.settings.config.getInt("bid-service.port")

    val bindingFuture = Http()
      .newServerAt(interface = interface, port = port)
      .bind(routes)

    bindingFuture.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server running at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoints, terminating ... ", ex)
        system.terminate()
    }(system.executionContext)
  }

}
