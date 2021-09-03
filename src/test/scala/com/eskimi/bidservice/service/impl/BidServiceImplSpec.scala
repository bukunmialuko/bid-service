package com.eskimi.bidservice.service.impl


import com.eskimi.bidservice.model._
import com.eskimi.bidservice.service.{BidResult, CampaignService}
import com.eskimi.bidservice.fixture.Fixtures._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BidServiceImplSpec extends AnyWordSpecLike with Matchers {

  private val bidRequestService = new BidServiceImpl(CampaignService.activeCampaigns)

  "get country" should {
    "return some country" when {
      "device geo country is set" in {
        bidRequestService.getCountry(bidRequest) should be(geo.country)
      }
      "user geo country is set and device is empty" in {
        bidRequestService.getCountry(bidRequest.copy(device = None)) should be(geo.country)
      }
    }
    "return None" when {
      "both geo country is empty" in {
        val geoEmpty = Geo(country = None)
        val user = bidUser.copy(geo = Some(geoEmpty))
        val dev = device.copy(geo = Some(geoEmpty))
        val request = bidRequest.copy(
          user = Some(user),
          device = Some(dev)
        )

        bidRequestService.getCountry(request) should be(None)
      }
      "geo is empty" in {
        val bidRequest1 = bidRequest.copy(
          user = Some(bidUser.copy(geo = None)),
          device = Some(device.copy(geo = None))
        )

        bidRequestService.getCountry(bidRequest1) should be(None)
      }
      "device and user are empty" in {
        val bidRequest1 = bidRequest.copy(
          user = None,
          device = None
        )

        bidRequestService.getCountry(bidRequest1) should be(None)
      }
    }
  }

  "price Banners" should {
    "return list of priced banners" when {
      "it fits the size of one of the impressions" in {
        val imp = impression.copy(
          wmin = Some(300),
          wmax = Some(700),
          w = None,
          hmin = Some(250),
          hmax = Some(400),
          h = None
        )
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp)),
        )

        val expectedBannersWithPrice = List(
          BidResult(campaign.id.toString, bidFloor, campaign.banners.head),
          BidResult(campaign.id.toString, bidFloor, campaign.banners(1))
        )
        bidRequestService.priceBanners(bidRequest1, campaign) should be (expectedBannersWithPrice)
      }
      "it fits the size of every impression" in {
        val bidFloor1 = bidFloor
        val imp1 = impression.copy(
          w = Some(700),
          h = Some(400),
          bidFloor = Some(bidFloor1)
        )
        val bidFloor2 = 2.66
        val imp2 = impression.copy(
          bidFloor = Some(bidFloor2)
        )
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp1, imp2)),
        )

        val expectedBannersWithPrice = List(
          BidResult(campaign.id.toString, bidFloor1, campaign.banners.head),
          BidResult(campaign.id.toString, bidFloor2, campaign.banners(1))
        )
        bidRequestService.priceBanners(bidRequest1, campaign) should be (expectedBannersWithPrice)
      }
    }
    "return list with single banner" when {
      "only some of the list banners fit" in {
        val imp = impression.copy(
          w = Some(700),
          h = Some(400)
        )
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp))
        )

        val expectedResult = List(
          BidResult(campaign.id.toString, bidFloor, campaign.banners.head)
        )
        bidRequestService.priceBanners(bidRequest1, campaign) should be(expectedResult)
      }
    }
    "return empty list" when {
      "none of the campaign banners fit the size" in {
        val imp = impression.copy(w = Some(777))
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp))
        )
        bidRequestService.priceBanners(bidRequest1, campaign) should be(List.empty)
      }
    }
  }
  "processBid" should {
    "return result with campaign id and list of priced banners" when {
      "request bid matches campaign" in {

        val expectedBanner = campaign.banners(2)
        val expectedCampId = campaign.id.toString

        bidRequestService.processBid(bidRequest) should be(Some(
          BidResult(expectedCampId, bidFloor, expectedBanner)
        )
        )
      }
    }
    "return None" when {
      "request bid country not matches any campaign" in {
        val geoKz = Geo(country = Some("KZ"))
        val bidRequest1 = bidRequest.copy(
          user = Some(bidUser.copy(geo = Some(geoKz))),
          device = Some(device.copy(geo = Some(geoKz)))
        )

        bidRequestService.processBid(bidRequest1) should be (None)
      }
      "request bid floor not matches any campaign" in {
        val imp = impression.copy(
          bidFloor = Some(14.66613)
        )
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp))
        )

        bidRequestService.processBid(bidRequest1) should be (None)
      }

      "request bid siteId not matches any campaign" in {
        val bidRequest1 = bidRequest.copy(
          site = site.copy(id = "siteId")
        )

        bidRequestService.processBid(bidRequest1) should be (None)
      }

      "request bid impression size not matches any campaign banner size" in {
        val imp = impression.copy(w = Some(888))
        val bidRequest1 = bidRequest.copy(
          imp = Some(List(imp))
        )

        bidRequestService.processBid(bidRequest1) should be (None)
      }
    }
  }
}
