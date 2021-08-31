package com.eskimi.bidservice.models

case class BidResponse(
                        id: String,
                        bidRequestId: String,
                        price: Double,
                        adid: Option[String],
                        banner: Option[Banner])