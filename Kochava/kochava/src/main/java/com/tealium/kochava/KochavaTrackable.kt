package com.tealium.kochava

import android.app.Application
import org.json.JSONObject

interface KochavaTrackable {


    // standard events
    // Gaming
    fun tutorialLevelComplete(eventName: String, userId: String = "", name: String = "", duration: Double = 0.0)
    fun purchase(userId: String = "", name: String = "", contentId: String = "", price: Double = 0.0, currency: String = "", guestCheckout: String = "")
    fun adview(type: String = "", networkName: String = "", placement: String = "", mediationName: String = "", campaignId: String = "", campaignName: String = "", size: String = "")
    fun rating(value: Double, maxRating: Double)

    // ecommerce
    fun addToCart(userId: String = "", name: String = "", contentId: String = "", quantity: Double = 0.0, referralForm: String = "")
    fun addToWishList(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")
    fun checkoutStart(userId: String = "", name: String = "", contentId: String = "", guestCheckout: String = "", currency: String = "")
    fun search(URI: String, results: String)

    // ride hailing
    fun registrationComplete(userId: String = "", userName: String = "", referralForm: String = "")

    // travel
    fun view(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")

    // utility
    fun achievement(userId: String = "", name: String = "", duration: Double = 0.0)

    // custom events
    fun customEvent(eventName: String, parameters: String)

}