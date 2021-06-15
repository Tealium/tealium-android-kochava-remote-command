package com.tealium.remotecommands.kochava

import android.app.Application
import android.net.Uri
import com.kochava.base.Tracker
import org.json.JSONObject
import java.util.*

class KochavaInstance(private val application: Application, private val applicationId: String) :
    KochavaCommand {

    override fun initialize(parameters: JSONObject?) {
        val config = Tracker.Configuration(application).apply {
            parameters?.let {
                val appGuid = it.optString(EventKey.APP_GUID, "")
                val limitAds = it.optBoolean(EventKey.LIMIT_AD_TRACKING, false)
                val idLinks = it.optJSONObject(EventKey.IDENTITY_LINKS)
                val logLevel = it.optString(EventKey.LOG_LEVEL, "")

                if (!appGuid.isNullOrEmpty()) {
                    setAppGuid(appGuid)
                }

                setAppLimitAdTracking(limitAds)

                idLinks?.let { links ->
                    if (links.length() > 0) {
                        setIdentityLink(links)
                    }
                }

                if (!logLevel.isNullOrEmpty()) {
                    val kLogLevel = getKochavaLogLevel(logLevel)
                    setLogLevel(kLogLevel)
                }
            }
        }

        Tracker.configure(config)
    }

    override fun sendEvent(standardEventType: Int, parameters: JSONObject?) {
        val event = Tracker.Event(standardEventType).apply {
            parameters?.let {
                val keys = it.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = it.opt(key)
                    addStandardParameter(key, value)
                }
            }
        }

        Tracker.sendEvent(event)
    }

    override fun sendCustomEvent(eventName: String, parameters: JSONObject?) {
        val event = Tracker.Event(eventName).apply {
            parameters?.let {
//            event = event.addCustom(it)
                // check if I can get away with just line above!!
                // else use smart-cast 'when' from addEventData()
                val keys = it.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = it.opt(key)
                    addCustomParameter(key, value)
                }
            }
        }

        Tracker.sendEvent(event)
    }

    override fun setSleep(sleep: Boolean) {
        Tracker.setSleep(sleep)
    }

    override fun setIdentityLink(identity: JSONObject) {
        val idLink = Tracker.IdentityLink().apply {
            val keys = identity.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = identity.optString(key)

                if (!value.isNullOrEmpty()) {
                    // add returns 'this' (Tracker.IdentityLink) --
                    // test is multiple key/value pairs are saved
                    add(key, value)
                }
            }
        }
        Tracker.setIdentityLink(idLink)
    }

    private fun getKochavaLogLevel(level: String): Int {
        return when (level) {
            "debug" -> Tracker.LOG_LEVEL_DEBUG
            "error" -> Tracker.LOG_LEVEL_ERROR
            "info" -> Tracker.LOG_LEVEL_INFO
            "never" -> Tracker.LOG_LEVEL_NONE
            "trace" -> Tracker.LOG_LEVEL_TRACE
            "warn" -> Tracker.LOG_LEVEL_WARN
            else -> Tracker.LOG_LEVEL_NONE
        }
    }

    private fun Tracker.Event.addCustomParameter(key: String, value: Any) {
        when (value) {
            is String -> this.addCustom(key, value)
            is Boolean -> this.addCustom(key, value)
            is Double -> this.addCustom(key, value)
            is Long -> this.addCustom(key, value)
            is Date -> this.addCustom(key, value)
            is JSONObject -> this.addCustom(value)
            else -> {
                this.addCustom(key, value as String)
            }
        }
    }

    private fun Tracker.Event.addStandardParameter(key: String, value: Any) {
        when (key) {
            ParameterKey.DEVICE_TYPE -> this.setAdDeviceType(value as String)
            ParameterKey.PLACEMENT -> this.setAdPlacement(value as String)
            ParameterKey.AD_TYPE -> this.setAdType(value as String)
            ParameterKey.AD_CAMPAIGN_ID -> this.setAdCampaignId(value as String)
            ParameterKey.AD_CAMPAIGN_NAME -> this.setAdCampaignName(value as String)
            ParameterKey.AD_SIZE -> this.setAdSize(value as String)
            ParameterKey.AD_GROUP_NAME -> this.setAdGroupName(value as String)
            ParameterKey.AD_GROUP_ID -> this.setAdGroupId(value as String)
            ParameterKey.AD_NETWORK_NAME -> this.setAdNetworkName(value as String)
            ParameterKey.AD_MEDIATION_NAME -> this.setAdMediationName(value as String)
            ParameterKey.CHECKOUT_AS_GUEST -> this.setCheckoutAsGuest(value as String)
            ParameterKey.CONTENT_ID -> this.setContentId(value as String)
            ParameterKey.CONTENT_TYPE -> this.setContentType(value as String)
            ParameterKey.CURRENCY -> this.setCurrency(value as String)
            ParameterKey.DATE -> {
                when (value) {
                    is Date -> this.setDate(value)
                    else -> this.setDate(value as String)
                }
            }
            ParameterKey.DESCRIPTION -> this.setDescription(value as String)
            ParameterKey.DESTINATION -> this.setDestination(value as String)
            ParameterKey.DURATION -> this.setDuration(value as Double)
            ParameterKey.END_DATE -> {
                when (value) {
                    is Date -> this.setEndDate(value)
                    else -> this.setEndDate(value as String)
                }
            }
            ParameterKey.ITEM_ADDED_FROM -> this.setItemAddedFrom(value as String)
            ParameterKey.LEVEL -> this.setLevel(value as String)
            ParameterKey.MAX_RATING_VALUE -> this.setMaxRatingValue(value as Double)
            ParameterKey.NAME -> this.setName(value as String)
            ParameterKey.ORDER_ID -> this.setOrderId(value as String)
            ParameterKey.ORIGIN -> this.setOrigin(value as String)
            ParameterKey.PRICE -> this.setPrice(value as Double)
            ParameterKey.QUANTITY -> this.setQuantity(value as Double)
            ParameterKey.RATING_VALUE -> this.setRatingValue(value as Double)
            ParameterKey.RECEIPT_ID -> this.setReceiptId(value as String)
            ParameterKey.REFERRAL_FROM -> this.setReferralFrom(value as String)
            ParameterKey.REGISTRATION_METHOD -> this.setRegistrationMethod(value as String)
            ParameterKey.RESULTS -> this.setResults(value as String)
            ParameterKey.SCORE -> this.setScore(value as String)
            ParameterKey.SEARCH_TERM -> this.setSearchTerm(value as String)
            ParameterKey.START_DATE -> {
                when (value) {
                    is Date -> this.setStartDate(value)
                    else -> this.setStartDate(value as String)
                }
            }
            ParameterKey.SUCCESS -> this.setSuccess(value as String)
            ParameterKey.USER_ID -> this.setUserId(value as String)
            ParameterKey.USER_NAME -> this.setUserName(value as String)
            ParameterKey.VALIDATED -> this.setValidated(value as String)
            ParameterKey.SPATIAL_X -> this.setSpatialX(value as Double)
            ParameterKey.SPATIAL_Y -> this.setSpatialY(value as Double)
            ParameterKey.SPATIAL_Z -> this.setSpatialZ(value as Double)

            ParameterKey.BACKGROUND -> this.setBackground(value as Boolean)
            ParameterKey.ACTION -> this.setAction(value as String)
            ParameterKey.COMPLETED -> this.setCompleted(value as Boolean)
            ParameterKey.URI -> {
                when (value) {
                    is Uri -> this.setUri(value)
                    else -> this.setUri(value as String)
                }
            }
            // not a standard param, add as custom
            else -> addCustomParameter(key, value)
        }
    }
}