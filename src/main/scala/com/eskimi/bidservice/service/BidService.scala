package com.eskimi.bidservice.service

import com.eskimi.bidservice.models.{Banner, BidRequest}


final case class BidResult(campaignId: String, bidFloor: Double, banner: Banner)

trait BidService {
  def processBid(bidRequest: BidRequest): Option[BidResult]
}
