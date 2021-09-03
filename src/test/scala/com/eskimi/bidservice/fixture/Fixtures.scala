package com.eskimi.bidservice.fixture

import com.eskimi.bidservice.model._
import com.eskimi.bidservice.service.CampaignService

object Fixtures {
  val bidFloor = 3.12123
  val impression = Impression(
    id = "1",
    wmin = Some(50),
    wmax = Some(300),
    w = Some(300),
    hmin = Some(100),
    hmax = Some(300),
    h = Some(250),
    bidFloor = Some(bidFloor)
  )
  val site = Site(
    id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
    domain = "fake.tld"
  )
  val geo = Geo(
    country = Some("LT")
  )
  val bidUser = User(
    id = "USARIO1",
    geo = Some(geo)
  )
  val device = Device(
    id = "440579f4b408831516ebd02f6e1c31b4",
    geo = Some(geo)
  )
  val requestId = "SGu1Jpq1IO"
  val bidRequest = BidRequest(
    id = requestId,
    imp = Some(List(impression)),
    site = site,
    user = Some(bidUser),
    device = Some(device)
  )
  val banner = Banner(
    id = 1,
    src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
    width = 300,
    height = 250
  )
  val campaign = CampaignService.activeCampaigns.head

}
