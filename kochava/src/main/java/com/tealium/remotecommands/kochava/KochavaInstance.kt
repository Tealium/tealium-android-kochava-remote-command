package com.tealium.remotecommands.kochava

import android.app.Application
import android.util.Log
import com.kochava.base.Tracker
import org.json.JSONObject
import java.util.*

class KochavaInstance(private val application: Application, private val appGuid: String? = null) :
    KochavaCommand {

    override fun initialize(parameters: JSONObject?) {
        val config = Tracker.Configuration(application).apply {
            parameters?.let {
                val appGuidParam = it.optString(EventKey.APP_GUID, "")
                val limitAdsParam = it.optBoolean(EventKey.LIMIT_AD_TRACKING, false)
                val idLinksParam = it.optJSONObject(EventKey.IDENTITY_LINKS)
                val logLevelParam = it.optString(EventKey.LOG_LEVEL, "")

                if (!appGuidParam.isNullOrEmpty()) {
                    setAppGuid(appGuidParam)
                } else {
                    appGuid?.let { id ->
                        setAppGuid(id)
                    } ?: kotlin.run {
                        Log.e(BuildConfig.TAG, "${EventKey.APP_GUID} is a required key")
                    }
                }

                setAppLimitAdTracking(limitAdsParam)

                idLinksParam?.let { links ->
                    if (links.length() > 0) {
                        setIdentityLink(links)
                    }
                }

                if (!logLevelParam.isNullOrEmpty()) {
                    val kLogLevel = getKochavaLogLevel(logLevelParam)
                    setLogLevel(kLogLevel)
                }
            } ?: run {
                appGuid?.let {
                    setAppGuid(it)
                } ?: kotlin.run {
                    Log.e(BuildConfig.TAG, "${EventKey.APP_GUID} is a required key")
                }
            }
        }

        Tracker.configure(config)
    }

    override fun sendEvent(
        standardEventType: Int,
        standardParameters: JSONObject?,
        customParameters: JSONObject?
    ) {
        val event = Tracker.Event(standardEventType).apply {
            standardParameters?.let {
                addStandardParameter(it)
            }

            customParameters?.let {
                addCustomParameter(it)
            }
        }

        Tracker.sendEvent(event)
    }

    override fun sendCustomEvent(eventName: String, parameters: JSONObject?) {
        val event = Tracker.Event(eventName).apply {
            parameters?.let {
                addCustomParameter(it)
            }
        }
        parameters?.let {
            Tracker.sendEvent(event)
        } ?: run {
            Tracker.sendEvent(eventName, "")
        }


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

    private fun Tracker.Event.addCustomParameter(parameters: JSONObject) {
        val keys = parameters.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            when (val value = parameters.opt(key)) {
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
    }

    private fun Tracker.Event.addStandardParameter(parameters: JSONObject) {
        val keys = parameters.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val valueParam = parameters.opt(key)
            valueParam?.let {
                val value = it.toString()
                when (key) {
                    ParameterKey.DEVICE_TYPE -> setAdDeviceType(value)
                    ParameterKey.PLACEMENT -> setAdPlacement(value)
                    ParameterKey.AD_TYPE -> setAdType(value)
                    ParameterKey.AD_CAMPAIGN_ID -> setAdCampaignId(value)
                    ParameterKey.AD_CAMPAIGN_NAME -> setAdCampaignName(value)
                    ParameterKey.AD_SIZE -> setAdSize(value)
                    ParameterKey.AD_GROUP_NAME -> setAdGroupName(value)
                    ParameterKey.AD_GROUP_ID -> setAdGroupId(value)
                    ParameterKey.AD_NETWORK_NAME -> setAdNetworkName(value)
                    ParameterKey.AD_MEDIATION_NAME -> setAdMediationName(value)
                    ParameterKey.CHECKOUT_AS_GUEST -> setCheckoutAsGuest(value)
                    ParameterKey.CONTENT_ID -> setContentId(value)
                    ParameterKey.CONTENT_TYPE -> setContentType(value)
                    ParameterKey.CURRENCY -> setCurrency(value)
                    ParameterKey.DATE -> setDate(value)
                    ParameterKey.DESCRIPTION -> setDescription(value)
                    ParameterKey.DESTINATION -> setDestination(value)
                    ParameterKey.DURATION -> setDuration(value.toDouble())
                    ParameterKey.END_DATE -> setEndDate(value)
                    ParameterKey.ITEM_ADDED_FROM -> setItemAddedFrom(value)
                    ParameterKey.LEVEL -> setLevel(value)
                    ParameterKey.MAX_RATING_VALUE -> setMaxRatingValue(value.toDouble())
                    ParameterKey.NAME -> setName(value)
                    ParameterKey.ORDER_ID -> setOrderId(value)
                    ParameterKey.ORIGIN -> setOrigin(value)
                    ParameterKey.PRICE -> setPrice(value.toDouble())
                    ParameterKey.QUANTITY -> setQuantity(value.toDouble())
                    ParameterKey.RATING_VALUE -> setRatingValue(value.toDouble())
                    ParameterKey.RECEIPT_ID -> setReceiptId(value)
                    ParameterKey.REFERRAL_FROM -> setReferralFrom(value)
                    ParameterKey.REGISTRATION_METHOD -> setRegistrationMethod(value)
                    ParameterKey.RESULTS -> setResults(value)
                    ParameterKey.SCORE -> setScore(value)
                    ParameterKey.SEARCH_TERM -> setSearchTerm(value)
                    ParameterKey.START_DATE -> setStartDate(value)
                    ParameterKey.SUCCESS -> setSuccess(value)
                    ParameterKey.USER_ID -> setUserId(value)
                    ParameterKey.USER_NAME -> setUserName(value)
                    ParameterKey.VALIDATED -> setValidated(value)
                    ParameterKey.SPATIAL_X -> setSpatialX(value.toDouble())
                    ParameterKey.SPATIAL_Y -> setSpatialY(value.toDouble())
                    ParameterKey.SPATIAL_Z -> setSpatialZ(value.toDouble())

                    ParameterKey.BACKGROUND -> setBackground(value.toBoolean())
                    ParameterKey.ACTION -> setAction(value)
                    ParameterKey.COMPLETED -> setCompleted(value.toBoolean())
                    ParameterKey.URI -> setUri(value)
                    else -> {
                    }
                }
            }
        }
    }
}