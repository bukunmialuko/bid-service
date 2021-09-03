package com.eskimi.bidservice.service.impl

import com.eskimi.bidservice.model.Impression
import com.eskimi.bidservice.service.{BannerService, CampaignService}
import com.eskimi.bidservice.CommonFixture._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike


class BannerServiceSpec extends AnyWordSpecLike
  with Matchers {
  "validateSize" should {
    "return true" when {
      "request value equals banner value" in {

        val imp = impression.copy(w = Some(350))
        val bannerValue = 350
        BannerService.validateBannerSize(
          requestSize = imp.w,
          requestMin = imp.wmin,
          requestMax = imp.wmax,
          bannerSize = bannerValue
        ) should be(true)
      }

      "request value not set and banner value is between min and max" in {
        val imp = impression.copy(
          wmax = Some(400),
          w = None
        )
        val bannerValue = 350
        BannerService.validateBannerSize(
          requestSize = imp.w,
          requestMin = imp.wmin,
          requestMax = imp.wmax,
          bannerSize = bannerValue
        ) should be(true)
      }
    }

    "return false" when {
      "request value greater than banner value" in {
        val bannerValue = 250
        BannerService.validateBannerSize(
          requestSize = impression.w,
          requestMin = impression.wmin,
          requestMax = impression.wmax,
          bannerSize = bannerValue
        ) should be(false)
      }

      "request value not passed and banner value in range of min and max values" in {
        val bannerValue = 250
        BannerService.validateBannerSize(
          requestSize = impression.w,
          requestMin = impression.wmin,
          requestMax = impression.wmax,
          bannerSize = bannerValue
        ) should be(false)
      }
    }
  }
  "getBanners" should {
    "return list of banners" when {
      "it fits the size" in {
        val imp = impression.copy(
          wmin = Some(300),
          wmax = Some(700),
          w = None,
          hmin = Some(250),
          hmax = Some(400),
          h = None
        )
        val campaignBanners = CampaignService.activeCampaigns(2).banners
        BannerService.getBanners(imp, campaignBanners) should be (campaignBanners)
      }
    }
    "return list with single banner" when {
      "some of the list's banners fit the size" in {
        val imp = impression.copy(
          w = Some(700),
          h = Some(400)
        )
        val campaignBanners = CampaignService.activeCampaigns(2).banners
        val banner1 = CampaignService.activeCampaigns(2).banners.head
        BannerService.getBanners(imp, campaignBanners) should be (List(banner1))
      }
    }

    "return empty list" when {
      "none of the list's banners fit the size" in {
        val imp = impression.copy(w = Some(800))
        val campaignBanners = CampaignService.activeCampaigns(2).banners
        BannerService.getBanners(imp, campaignBanners) should be (List.empty)
      }
    }
  }

}
