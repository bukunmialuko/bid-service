package com.eskimi.bidservice

import akka.actor.typed.ActorSystem
import com.eskimi.bidservice.actor.RootActor

object Main extends App {
  ActorSystem[Nothing](RootActor(), "BidServiceHttpServer")
}
