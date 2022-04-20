package com.example.offerxp

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.offerxp.models.Reward
import com.offerxp.offerxp_sdk.R
import kotlinx.android.synthetic.main.layout_offer_dialog.*


class OfferDialog(val activity: Activity, val reward: Reward) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_offer_dialog)

        getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        close_btn.setOnClickListener {
            dismiss()
        }
        Glide.with(this.context).load(reward.rewardDetails.banner).transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().override(100, 100)
                        .override(Target.SIZE_ORIGINAL))
                .into(reward_banner_iv)
        reward_title_tv.text = reward.rewardDetails.campaignTitle
        reward_subtitle_tv.text = reward.rewardDetails.campaignDescription
        reward_coupon_code_tv.text = reward.couponCode
        redeemBtn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reward.rewardDetails.link))
            activity.startActivity(browserIntent)
            dismiss()
        }
    }
}