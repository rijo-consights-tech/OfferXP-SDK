package com.example.offerxp.models

import com.google.gson.annotations.SerializedName

data class OfferXpJwsPayload(@SerializedName("payload") val payload: String?)
data class OfferXpOrderData(@SerializedName("order_id") val orderId: String,
                            @SerializedName("order_price") val orderPrice: Double,
                            @SerializedName("order_qty") val orderQty: Int)
