package com.eskimi.bidservice.service
import com.eskimi.bidservice.model.{Banner, Impression}

object BannerService {
  def getBanners(impression: Impression, banners: List[Banner]): List[Banner] = banners.filter(banner =>
    validateBannerSize(
      impression.h,
      impression.hmin,
      impression.hmax,
      banner.height
    ) &&
      validateBannerSize(
        impression.w,
        impression.wmin,
        impression.wmax,
        banner.width)
  )

  def validateBannerSize(requestSize: Option[Int],
                         requestMin: Option[Int],
                         requestMax: Option[Int],
                         bannerSize: Int): Boolean =
    (requestSize, requestMin, requestMax)  match {
      case (Some(size), _, _) => size == bannerSize
      case (_, Some(min), Some(max)) => min to max contains bannerSize
      case (_, Some(min), _) => bannerSize >= min
      case (_, _, Some(max)) => bannerSize <= max
      case _ => false
    }

}
