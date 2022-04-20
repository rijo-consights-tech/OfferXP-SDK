package com.example.offerxp

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import com.offerxp.offerxp_sdk.R
import kotlin.coroutines.coroutineContext

class OfferXPView : WebView {
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        LayoutInflater.from(context)
                .inflate(R.layout.offerxp_view_layout, this, true)

    }

}
