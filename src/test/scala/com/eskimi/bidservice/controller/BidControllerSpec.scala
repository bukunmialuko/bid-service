package com.eskimi.bidservice.controller

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.eskimi.bidservice.actor.BidActor
import com.eskimi.bidservice.model._
import com.eskimi.bidservice.service.{BidResult, BidService}
import com.eskimi.bidservice.utils.JsonSupport
import org.mockito.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.eskimi.bidservice.fixture.Fixtures._
import com.eskimi.bidservice.controller.BidController

class BidControllerSpec extends AnyWordSpecLike
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with MockitoSugar
  with JsonSupport {

  private lazy val testKit = ActorTestKit()
  private implicit def typedSystem = testKit.system

  private val bidServiceMock = mock[BidService]
  private val bidActor = testKit.spawn(BidActor(bidServiceMock))
  private lazy val routes = new BidController(bidActor).routes

  private val BidPath = "/bid"

  "POST" should {
    "return 200 and response body if bid is successful" in {

      val bidEntity = Marshal(bidRequest).to[MessageEntity].futureValue
      when(bidServiceMock.processBid(bidRequest))
        .thenReturn(Some(BidResult("1", bidFloor, banner)))

      val request = Post(BidPath).withEntity(bidEntity)

      request ~> routes ~> check {
        status should be (StatusCodes.OK)

        contentType should be (ContentTypes.`application/json`)

      }
    }
    "return 204 and no response body if not bidding" in {
      val bidEntity = Marshal(bidRequest).to[MessageEntity].futureValue
      when(bidServiceMock.processBid(bidRequest)).thenReturn(None)

      val request = Post(BidPath).withEntity(bidEntity)

      request ~> routes ~> check {
        status should be (StatusCodes.NoContent)

        entityAs[String] shouldEqual ""

      }
    }
  }
}
