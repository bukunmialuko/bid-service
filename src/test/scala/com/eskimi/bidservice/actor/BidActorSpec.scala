package com.eskimi.bidservice.actor

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.eskimi.bidservice.actor
import BidActor.{BidRequestMessage, BidResponseMessage}
import com.eskimi.bidservice.model._
import com.eskimi.bidservice.service.{BidResult, BidService}
import org.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpecLike
import com.eskimi.bidservice.fixture.Fixtures._

class BidActorSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike  with MockitoSugar {

  private val bidServiceMock = mock[BidService]

  "A BidActor" must {
    "reply with bid response in case of successful bid" in {
      val adId = "1"
      val bidResponse = BidResponseMessage(
        Some(BidResponse(
          id = "response1",
          bidRequestId = requestId,
          price = bidFloor,
          adid = Some(adId),
          banner = Some(banner)
        ))
      )
      val replyProbe = createTestProbe[BidResponseMessage]()
      val underTest = spawn(BidActor(bidServiceMock))
      when(bidServiceMock.processBid(bidRequest)).thenReturn(
        Some(BidResult(adId, bidFloor, banner))
      )
      underTest ! BidRequestMessage(bidRequest, replyProbe.ref)
      replyProbe.expectMessage(bidResponse)
    }

    "reply with empty response if not bid" in {
      val bidResponse = BidResponseMessage(None)
      val replyProbe = createTestProbe[BidResponseMessage]()
      val underTest = spawn(actor.BidActor(bidServiceMock))
      when(bidServiceMock.processBid(bidRequest)).thenReturn(None)
      underTest ! BidRequestMessage(bidRequest, replyProbe.ref)
      replyProbe.expectMessage(bidResponse)
    }

  }

}
