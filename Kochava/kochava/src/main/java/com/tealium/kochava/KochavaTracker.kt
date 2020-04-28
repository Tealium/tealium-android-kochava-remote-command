package com.tealium.kochava

import android.app.Application
import com.kochava.base.Tracker
import org.json.JSONObject

class KochavaTracker(application: Application, applicationId: String) : KochavaTrackable {


    init {
        Tracker.configure(Tracker.Configuration(application.applicationContext)
            .setAppGuid(applicationId)
        )
    }

    override fun tutorialLevelComplete(eventName: String, userId: String, name: String, duration: Double) {
        Tracker.sendEvent(
            Tracker.Event(Tracker.EVENT_TYPE_TUTORIAL_COMPLETE)
                .setUserId(userId)
                .setName(name)
                .setDuration(duration)
        )
    }

    override fun purchase (userId: String, name: String, contentId: String, price: Double, currency: String, guestCheckout: String) {
        Tracker.sendEvent(
            Tracker.Event(Tracker.EVENT_TYPE_PURCHASE)
                .setUserId(userId)
                .setName(name)
                .setContentId(contentId)
                .setPrice(price)
                .setCurrency(currency)
                .setCheckoutAsGuest(guestCheckout)
        )
    }

    override fun adview(
        type: String,
        networkName: String,
        placement: String,
        mediationName: String,
        campaignId: String,
        campaignName: String,
        size: String
    ) {
        Tracker.sendEvent(
            Tracker.Event(Tracker.EVENT_TYPE_AD_VIEW)
                .setAdType(type)
                .setAdNetworkName(networkName)
                .setAdPlacement(placement)
                .setAdMediationName(mediationName)
                .setAdCampaignId(campaignId)
                .setAdCampaignName(campaignName)
                .setAdSize(size)
        )
    }

    override fun rating(value: Double, maxRating: Double) {
        Tracker.sendEvent(
            Tracker.Event(Tracker.EVENT_TYPE_RATING)
                .setRatingValue(value)
                .setMaxRatingValue(maxRating)
        )
    }

    override fun addToCart(
        userId: String,
        name: String,
        contentId: String,
        quantity: Double,
        referralForm: String
    ) {
        Tracker.sendEvent(
            Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_CART)
                .setUserId(userId)
                .setName(name)
                .setContentId(contentId)
                .setQuantity(quantity)
                .setReferralFrom(referralForm)
        )
    }

    override fun addToWishList(
        userId: String,
        name: String,
        contentId: String,
        referralForm: String
    ) {
        Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_WISH_LIST)
            .setUserId(userId)
            .setName(name)
            .setContentId(contentId)
            .setReferralFrom(referralForm)
    }

    override fun checkoutStart(
        userId: String,
        name: String,
        contentId: String,
        guestCheckout: String,
        currency: String
    ) {
        Tracker.Event(Tracker.EVENT_TYPE_CHECKOUT_START)
            .setUserId(userId)
            .setName(name)
            .setContentId(contentId)
            .setCheckoutAsGuest(guestCheckout)
            .setCurrency(currency)
    }

    override fun search(URI: String, results: String) {
        Tracker.Event(Tracker.EVENT_TYPE_SEARCH)
            .setUri(URI)
            .setResults(results)
    }

    override fun registrationComplete(userId: String, userName: String, referralForm: String) {
        Tracker.Event(Tracker.EVENT_TYPE_REGISTRATION_COMPLETE)
            .setUserId(userId)
            .setUserName(userName)
            .setReferralFrom(referralForm)
    }

    override fun view(userId: String, name: String, contentId: String, referralForm: String) {
        Tracker.Event(Tracker.EVENT_TYPE_VIEW)
            .setUserId(userId)
            .setName(name)
            .setContentId(contentId)
            .setReferralFrom(referralForm)
    }

    override fun achievement(userId: String, name: String, duration: Double) {
        Tracker.Event(Tracker.EVENT_TYPE_ACHIEVEMENT)
            .setUserId(userId)
            .setName(name)
            .setDuration(duration)
    }

    override fun customEvent(eventName: String, parameters: String) {
        Tracker.sendEvent(eventName, parameters)
    }
}