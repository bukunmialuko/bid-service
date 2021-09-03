package com.eskimi.bidservice.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.eskimi.bidservice.model.{BidRequest, BidResponse}
import com.eskimi.bidservice.service.BidService

object BidActor {

  // Actor should send and receive messages
  sealed trait BidMessage
  // Bid request message of type BidMessage should hold a bid request and an actor to which it returns the
  // processed result of type BidResponseMessage to.
  final case class BidRequestMessage(bidRequest: BidRequest, replyTo: ActorRef[BidResponseMessage]) extends BidMessage
  final case class BidResponseMessage(bidResponse: Option[BidResponse]) extends BidMessage

  def apply(bidService: BidService): Behavior[BidRequestMessage] = bidAgent(0, bidService)

  private def bidAgent(bidCounter: Int, bidService: BidService): Behavior[BidRequestMessage] = {

    // Reacts.
    Behaviors.receiveMessage { message =>
      val responseId = bidCounter + 1
      val response = bidService.processBid(message.bidRequest).map { result =>
        BidResponse(
          id = s"response$responseId",
          bidRequestId = message.bidRequest.id,
          price = result.bidFloor,
          adId = Some(result.campaignId),
          banner = Some(result.banner)
        )
      }
      message.replyTo ! BidResponseMessage(response)
      bidAgent(responseId, bidService)
    }
  }
}
