package com.example.offerxp.models

import android.telecom.Call
import com.google.gson.annotations.SerializedName

data class Reward(
        @SerializedName("reward") val rewardDetails: RewardDetails,
        @SerializedName("coupon_code") val couponCode: String,
)

data class RewardResponse(
        @SerializedName("message") val message: String,
        @SerializedName("reward") val reward: Reward
)

data class Brand(
        @SerializedName("id") val id: Int,
        @SerializedName("logo") val logo: String,
        @SerializedName("name") val name: String,
        @SerializedName("registered_name") val registeredName: String
)

data class RewardType(
        @SerializedName("name") val name: String
)

data class RewardDetails(
        @SerializedName("brand") val brand: Brand,
        @SerializedName("campaign_title") val campaignTitle: String,
        @SerializedName("campaign_brief") val campaignBrief: String,
        @SerializedName("campaign_description") val campaignDescription: String,
        @SerializedName("campaign_tnc") val campaignTnc: String,
        @SerializedName("link") val link: String,
        @SerializedName("campaign_amount") val campaignAmount: String,
        @SerializedName("banner") val banner: String,
        @SerializedName("reward_type") val rewardType: RewardType,
)

