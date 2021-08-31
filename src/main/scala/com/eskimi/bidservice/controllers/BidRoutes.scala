package com.eskimi.bidservice.controllers

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes.NoContent
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.eskimi.bidservice.actors.BidActor.{BidRequestMessage, BidResponseMessage}
import com.eskimi.bidservice.models.BidRequest
import com.eskimi.bidservice.utils.JsonSupport

import scala.concurrent.Future

class BidRoutes(bidActor: ActorRef[BidRequestMessage])
               (implicit val system: ActorSystem[_])
  extends Directives with JsonSupport  {

  private implicit val timeout: Timeout =
    Timeout.create(system.settings.config.getDuration("bid-service.routes.ask-timeout"))

  // async, accepts result and returns a response
  def processBid(bidRequest: BidRequest): Future[BidResponseMessage] = bidActor.ask(replyTo =>
    BidRequestMessage(bidRequest, replyTo)
  )

  val routes: Route =
    pathPrefix("bid") {
      pathEnd {
        post {
          entity(as[BidRequest]) { bidRequestBody =>
            onSuccess(processBid(bidRequestBody)) {
              _.bidResponse.map{complete(_)}.
                getOrElse(complete(NoContent))
            }
          }
        }
      }
    }
}
