package com.tealium.kochava

import android.app.Application
import org.json.JSONObject

interface KochavaTrackable {

    fun configure(application: Application, parameters: JSONObject)
    // Standard events
    // Gaming
    fun tutorialComplete(userId: String = "", name: String = "", duration: Double)
    fun levelComplete(userId: String = "", name: String = "", duration: Double)
    fun purchase(userId: String = "", name: String = "", contentId: String = "", price: Double, currency: String = "", guestCheckout: String = "")
    fun adView(type: String = "", networkName: String = "", placement: String = "", mediationName: String = "", campaignId: String = "", campaignName: String = "", size: String = "")
    fun rating(value: Double, maxRating: Double)

    // Ecommerce
    fun addToCart(userId: String = "", name: String = "", contentId: String = "", quantity: Double, referralForm: String = "")
    fun addToWishList(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")
    fun checkoutStart(userId: String = "", name: String = "", contentId: String = "", guestCheckout: String = "", currency: String = "")
    fun search(URI: String, results: String)
    fun subscribe(price: Double, currency: String, productName: String, userId: String)
    fun startTrial(price: Double, currency: String, productName: String, userId: String)

    // Ride hailing
    fun registrationComplete(userId: String = "", userName: String = "", referralForm: String = "")

    // Travel
    fun view(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")

    // Utility
    fun achievement(userId: String = "", name: String = "", duration: Double)

    fun deepLink(URI: String)

    // Sleep tracker
    fun setSleep(sleep: Boolean)

    // Custom events
    fun customEvent(eventName: String, parameters: String)
}