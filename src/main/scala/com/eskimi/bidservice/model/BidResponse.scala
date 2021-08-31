package com.eskimi.bidservice.model

case class BidResponse(
                        id: String,
                        bidRequestId: String,
                        price: Double,
                        adid: Option[String],
                        banner: Option[Banner])