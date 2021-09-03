package com.eskimi.bidservice.model

case class Campaign(
                     id: Int,
                     country: String,
                     targeting: Targeting,
                     banners: List[Banner],
                     bid: Double)

case class Targeting(
                      targetedSiteIds: Set[String], impressionId: String)

case class Banner(
                   id: Int,
                   src: String,
                   width: Int,
                   height: Int)