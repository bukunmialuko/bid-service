package com.eskimi.bidservice.utils

import com.eskimi.bidservice.models._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

// Processes json for http requests
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val geoJsonFormat = jsonFormat1(Geo)
  implicit val bidUserJsonFormat = jsonFormat2(User)
  implicit val siteJsonFormat = jsonFormat2(Site)
  implicit val deviceJsonFormat = jsonFormat2(Device)
  implicit val impressionJsonFormat = jsonFormat8(Impression)
  implicit val bidRequestJsonFormat = jsonFormat5(BidRequest)

  implicit val bannerJsonFormat = jsonFormat4(Banner)
  implicit val bidResponseJsonFormat = jsonFormat5(BidResponse)
}
