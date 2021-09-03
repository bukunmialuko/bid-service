package com.eskimi.bidservice.service.impl
import com.eskimi.bidservice.model.{BidRequest, Campaign}
import com.eskimi.bidservice.service.{BannerService, BidResult, BidService}

class BidServiceImpl(campaigns: Seq[Campaign]) extends BidService {

  override def processBid(bidRequest: BidRequest): Option[BidResult] =
    (for {
      campaign <- campaigns if campaignMatches(campaign, bidRequest)
      results <- priceBanners(bidRequest, campaign)
    } yield results)
      .headOption

  private def campaignMatches(campaign: Campaign, bidRequest: BidRequest) =
    Option(campaign.country) == getCountry(bidRequest) && campaign.targeting.targetedSiteIds.contains(bidRequest.site.id)

  private[impl] def priceBanners(bidRequest: BidRequest, campaign: Campaign) =
    bidRequest.imp.map { impressions =>
      impressions.flatMap { impression =>
        val requestBid = impression.bidFloor.getOrElse(0.1d)
        if (requestBid <= campaign.bid)
          BannerService
            .getBanners(impression, campaign.banners)
            .map(BidResult(campaign.id.toString, requestBid, _))
        else List.empty
      }
    }
      .getOrElse(List.empty)

  // device.geo object has a higher priority than user.geo object
  private [impl] def getCountry(bidRequest: BidRequest): Option[String] =
    getDeviceCountry(bidRequest) orElse getUserCountry(bidRequest)

  private def getDeviceCountry(bidRequest: BidRequest) = for {
    device <- bidRequest.device
    geo <- device.geo
    country <- geo.country
  } yield country

  private def getUserCountry(bidRequest: BidRequest) = for {
      user <- bidRequest.user
      geo <- user.geo
      country <- geo.country
    } yield country

}
