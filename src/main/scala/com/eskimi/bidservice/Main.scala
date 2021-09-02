package com.eskimi.bidservice

import akka.actor.typed.ActorSystem
import com.eskimi.bidservice.actor.RootActor

object Main extends App {
  println("Hello Scala")
  ActorSystem[Nothing](RootActor(), "EskimiBidServer")

}
