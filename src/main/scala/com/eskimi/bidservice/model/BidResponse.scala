package com.eskimi.bidservice.model

case class BidResponse(
                        id: String,
                        bidRequestId: String,
                        price: Double,
                        adId: Option[String],
                        banner: Option[Banner])