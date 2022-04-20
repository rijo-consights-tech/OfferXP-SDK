package com.example.offerxp

import android.webkit.JsResult

import android.webkit.WebView

import android.webkit.WebChromeClient





 class OfferXPWebChromeClient : WebChromeClient() {
    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult): Boolean {
        //Log.d("LogTag", message)
        result.confirm()

        return true
    }
}