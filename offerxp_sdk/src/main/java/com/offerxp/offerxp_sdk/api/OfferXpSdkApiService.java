package com.offerxp.offerxp_sdk.api;

import com.example.offerxp.models.OfferXpJwsPayload;
import com.example.offerxp.models.Reward;
import com.example.offerxp.models.RewardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OfferXpSdkApiService {



    @POST("/shopify/sdk/transactions/")
    Call<RewardResponse> fetchNewReward(@Body OfferXpJwsPayload payload);

//    @POST("/shopify/sdk/offerxp/auth/")
//    @Headers({"Partner-Store:rijo-shopify-test.store.shopify.com", "Partner-API-key:63dac5d3-3f5c-4b7c-baf2-929131468945"})
//    Call<Object> getOfferXPAuthToken();
}
