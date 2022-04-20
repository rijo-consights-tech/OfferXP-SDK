package com.example.offerxp

import android.app.Activity
import android.webkit.ConsoleMessage
import androidx.annotation.NonNull

import com.example.offerxp.models.OfferXpJwsPayload
import com.example.offerxp.models.Reward
import com.example.offerxp.models.RewardResponse
import com.offerxp.offerxp_sdk.api.OfferXpSdkApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import java.util.*

object OfferXP {
    var jwt: String? = null
    var tokenExpiryListener: TokenExpiryListener? = null
    var partnerStore: String?=null
    var apiKey: String?=null
     private var lastOrderPayload: String?=null
     private var lastRewardListener: RewardListener?=null

    /**
     * env stands for environment.
     * This can be used for separate the development and production environment. As of now it support two values
     * 1) dev -  stands for development mode
     * 2) prod - stands for production mode
     * By default it will be configured to the dev mode
     */
    var env = "dev"

    /**
     * authenticate the user with the provided JWT auth-token
     * JWT auth-token is generated using the offerxp user-token
     *
     */

    fun authenticateUser(jwt: String, tokenExpiryListener: TokenExpiryListener) {
        //todo: change the function name to setUserCredentials
        this.jwt = jwt
        this.tokenExpiryListener = tokenExpiryListener
    }

    fun retryLastOrder(){
        /**
         * Call this method to retry the last transaction.
         * It will be helpful if the prev transaction got failed due to expired token or some other reasons
         */
        if(lastRewardListener!=null&& lastOrderPayload!=null){
            putOrder(lastOrderPayload!!, lastRewardListener!!)
        }
    }

    fun putOrder(jwsPayload: String, @NonNull rewardListener: RewardListener) {
        lastOrderPayload = jwsPayload
        lastRewardListener = rewardListener
        OfferXpSdkApiClient.getInstance().offerXpSdkApis.fetchNewReward(OfferXpJwsPayload(jwsPayload)).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(call: Call<RewardResponse>, response: Response<RewardResponse>) {
                if (response.isSuccessful) {
                    if (response.body() == null)
                        rewardListener.onNoRewardsAvailable("unexpected error")
                    else if(response.body()!!.message != "Reward Created")
                        rewardListener.onNoRewardsAvailable(response.body()!!.message)
                    else
                        rewardListener.onReceiveReward(response.body()!!.reward)

                } else {
                    rewardListener.onRewardFetchFailed(Exception(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                rewardListener.onRewardFetchFailed(Exception("unexpected error", t))
            }

        })
    }
    fun showReward(activity: Activity,reward: Reward){
        /**
         * This method will display a popup with a default UI representation of the reward
         */
        val dialog = OfferDialog(activity, reward)
        dialog.setCancelable(false)
        dialog.show()
    }
}

interface RewardListener {
    fun onReceiveReward(reward: Reward) {

    }

    fun onNoRewardsAvailable(message: String?) {

    }

    fun onRewardFetchFailed(error: Exception)
}
interface TokenExpiryListener {
    fun onTokenExpiry() {

    }
}