package com.eskimi.bidservice.service
import com.eskimi.bidservice.model.{Banner, Campaign, Targeting}

object CampaignService {
  def activeCampaigns: Seq[Campaign] = {
    val campaign1 = Campaign(
      id = 1,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Set(
          "0006a522ce0f4bbbbaa6b3c38cafaa0f"
        ),
        impressionId    = "1"
      ),
      banners = List(Banner(
        id = 1,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        width = 300,
        height = 250
      )
      ),
      bid = 5d
    )

    val campaign2 = campaign1.copy(
      id = 2,
      country = "GB",
      bid = 1.33d
    )

    val campaign3 = campaign1.copy(
      id = 3,
      country = "US",
      banners = List(Banner(
        id = 5,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph5.jpeg",
        width = 700,
        height = 400
      ),
        Banner(
          id = 6,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph6.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 3.25d
    )

    Seq(
      campaign1,
      campaign2,
      campaign3
    )
  }
}
