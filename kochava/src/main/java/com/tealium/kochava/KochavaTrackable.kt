package com.tealium.kochava

interface KochavaTrackable {

    // standard events
    // gaming
    fun tutorialLevelComplete(eventName: String, userId: String = "", name: String = "", duration: Double)
    fun purchase(userId: String = "", name: String = "", contentId: String = "", price: Double, currency: String = "", guestCheckout: String = "")
    fun adView(type: String = "", networkName: String = "", placement: String = "", mediationName: String = "", campaignId: String = "", campaignName: String = "", size: String = "")
    fun rating(value: Double, maxRating: Double)

    // ecommerce
    fun addToCart(userId: String = "", name: String = "", contentId: String = "", quantity: Double, referralForm: String = "")
    fun addToWishList(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")
    fun checkoutStart(userId: String = "", name: String = "", contentId: String = "", guestCheckout: String = "", currency: String = "")
    fun search(URI: String, results: String)

    // ride hailing
    fun registrationComplete(userId: String = "", userName: String = "", referralForm: String = "")

    // travel
    fun view(userId: String = "", name: String = "", contentId: String = "", referralForm: String = "")

    // utility
    fun achievement(userId: String = "", name: String = "", duration: Double)

    // custom events
    fun customEvent(eventName: String, parameters: String)

}