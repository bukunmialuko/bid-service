package com.eskimi.bidservice.actor

import akka.actor.typed.scaladsl.Behaviors
import com.eskimi.bidservice.controller.BidController
import com.eskimi.bidservice.service.impl.BidServiceImpl
import com.eskimi.bidservice.BidServiceHttpServer
import com.eskimi.bidservice.service.CampaignService


object RootActor {

  def apply() = Behaviors.setup[Nothing] { context =>
    val bidRequestService = new BidServiceImpl(CampaignService.activeCampaigns)
    val bidActor = context.spawn(BidActor(bidRequestService), "BidAgentActor")
    context.watch(bidActor)

    implicit val system = context.system
    val routes = new BidController(bidActor)
    BidServiceHttpServer.start(routes.routes)

    Behaviors.empty
  }

}
