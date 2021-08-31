package com.eskimi.bidservice.service

import com.eskimi.bidservice.models.{Banner, BidRequest}


final case class BidResult(campaignId: String, bidFloor: Double, banner: Banner)

trait BidService {
  /*
  Should accept a bid request of type BidRequest,
  process it and return a valid response of BidResult type.
   */
  def processBid(bidRequest: BidRequest): Option[BidResult]
}
