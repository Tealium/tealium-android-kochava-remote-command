package com.tealium.kochava

import android.app.Application
import org.json.JSONObject

interface KochavaTrackable {

    fun configure(application: Application, parameters: JSONObject)

    fun setSleep(sleep: Boolean)

    fun logEvent(
        eventName: String,
        payload: String,
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
    )

    fun customEvent(eventName: String, parameters: String)
}