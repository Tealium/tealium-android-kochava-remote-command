package com.tealium.kochava

import android.annotation.SuppressLint
import android.app.Application
import com.kochava.base.Tracker
import org.json.JSONObject

class KochavaTracker(application: Application, applicationId: String) : KochavaTrackable {

    init {
        Tracker.configure(Tracker.Configuration(application.applicationContext)
            .setAppGuid(applicationId)
        )
    }

    override fun configure(application: Application, configurationParams: JSONObject) {
        Tracker.configure(Tracker.Configuration(application.applicationContext)
            .addCustom(configurationParams)
        )
    }

    override fun setSleep(sleep: Boolean) {
        Tracker.setSleep(sleep)
    }

    @SuppressLint("CheckResult")
    override fun logEvent(
        eventName: String,
        deviceType: String,
        placement: String,
        adType: String,
        adCampaignId: String,
        adCampaignName: String,
        adSize: String,
        adGroupName: String,
        adGroupId: String,
        adNetworkName: String,
        adMediationName: String,
        checkoutAsGuest: String,
        contentId: String,
        contentType: String,
        currency: String,
        date: String,
        description: String,
        destination: String,
        duration: Double,
        source: String,
        uri: String,
        completed: Boolean,
        action: String,
        background: Boolean,
        spatialX: Double,
        spatialY: Double,
        spatialZ: Double,
        validated: String,
        userName: String,
        userId: String,
        success: String,
        startDate: String,
        endDate: String,
        searchTerm: String,
        source1: String,
        score: String,
        results: String,
        registrationMethod: String,
        referralFrom: String,
        receiptId: String,
        ratingValue: Double,
        quantity: Double,
        price: Double,
        origin: String,
        orderId: String,
        name: String,
        maxRatingValue: Double,
        level: String,
        itemAddedFrom: String
    ) {
        var event: Tracker.Event
        if (eventName.equals(Commands.ACHIEVEMENT)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ACHIEVEMENT)
        } else if (eventName.equals(Commands.AD_VIEW)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_AD_VIEW)
        } else if (eventName.equals(Commands.ADD_TO_CART)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_CART)
        } else if (eventName.equals(Commands.ADD_TO_WISH_LIST)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_WISH_LIST)
        } else if (eventName.equals(Commands.CHECKOUT_START)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_CHECKOUT_START)
        } else if (eventName.equals(Commands.DEEP_LINK)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_DEEP_LINK)
        } else if (eventName.equals(Commands.LEVEL_COMPLETE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_LEVEL_COMPLETE)
        } else if (eventName.equals(Commands.PURCHASE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_PURCHASE)
        } else if (eventName.equals(Commands.RATING)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_RATING)
        } else if (eventName.equals(Commands.SEARCH)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_SEARCH)
        } else if (eventName.equals(Commands.START_TRIAL)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_START_TRIAL)
        } else if (eventName.equals(Commands.SUBSCRIBE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_SUBSCRIBE)
        } else if (eventName.equals(Commands.TUTORIAL_COMPLETE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_TUTORIAL_COMPLETE)
        } else {
            event = Tracker.Event(Tracker.EVENT_TYPE_VIEW)
        }

        event.setAction(action)
            .setAdCampaignId(adCampaignId)
            .setAdCampaignName(adCampaignName)
            .setAdDeviceType(deviceType)
            .setAdGroupId(adGroupId)
            .setAdGroupName(adGroupName)
            .setAdMediationName(adMediationName)
            .setAdPlacement(placement)
            .setAdSize(adSize)
            .setAdType(adType)
            .setCheckoutAsGuest(checkoutAsGuest)
            .setCompleted(completed)
            .setContentId(contentId)
            .setContentType(contentType)
            .setCurrency(currency)
            .setStartDate(startDate)
            .setDescription(description)
            .setDestination(destination)
            .setEndDate(endDate)
            .setItemAddedFrom(itemAddedFrom)
            .setLevel(level)
            .setMaxRatingValue(maxRatingValue)
            .setName(name)
            .setOrderId(orderId)
            .setQuantity(quantity)
            .setOrigin(origin)
            .setRatingValue(ratingValue)
            .setReceiptId(receiptId)
            .setReferralFrom(referralFrom)
            .setRegistrationMethod(registrationMethod)
            .setSearchTerm(searchTerm)
            .setResults(results)
            .setScore(score)
            .setDate(date)
            .setSuccess(success)
            .setUserId(userId)
            .setUri(uri)
            .setValidated(validated)
            .setBackground(background)

        if (!price.isNaN()) event.setPrice(price)
        if (!duration.isNaN()) event.setDuration(duration)
        if (!spatialX.isNaN()) event.setSpatialX(spatialX)
        if (!spatialX.isNaN()) event.setSpatialX(spatialX)
        if (!spatialZ.isNaN()) event.setSpatialZ(spatialZ)
        Tracker.sendEvent(event)
    }

    override fun customEvent(eventName: String, parameters: String) {
        Tracker.sendEvent(eventName, parameters)
    }
}