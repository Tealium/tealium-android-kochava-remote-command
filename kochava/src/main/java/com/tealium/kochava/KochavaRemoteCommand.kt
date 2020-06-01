package com.tealium.kochava

import android.app.Application
import com.tealium.internal.tagbridge.RemoteCommand
import org.json.JSONObject


open class KochavaRemoteCommand @JvmOverloads constructor(
    application: Application,
    appGuid: String,
    commandId: String = DEFAULT_COMMAND_ID,
    description: String = DEFAULT_COMMAND_DESCRIPTION,
    var tracker: KochavaTrackable = KochavaTracker(
        application,
        appGuid
    )
) : RemoteCommand(commandId, description) {

    private val TAG = this::class.java.simpleName

    /**
     * Handles the incoming RemoteCommand response data. Prepares the command and payload for parsing.
     *
     * @param response - the response that includes the command and optional command parameters
     */
    @Throws(Exception::class)
    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }

    fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(Commands.COMMAND_KEY)
        return command.split(Commands.SEPARATOR.toRegex())
            .dropLastWhile {
                it.isEmpty()
            }.map {
                it.trim().toLowerCase()
            }
            .toTypedArray()
    }

    /**
     * Calls the individual commands consecutively with optional parameters from the payload object.
     *
     * @param commands - the list of commands to call
     * @param payload - optional command parameters to be called with specific commands
     */
    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->

            when (command) {
                Commands.PURCHASE -> {
                    purchase(payload)
                }
                Commands.TUTORIAL_COMPLETE -> {
                    tutorialComplete(payload)
                }
                Commands.LEVEL_COMPLETE -> {
                    levelComplete(payload)
                }
                Commands.AD_VIEW -> {
                    adView(payload)
                }
                Commands.RATING -> {
                    rating(payload)
                }
                Commands.ADD_TO_CART -> {
                    addToCart(payload)
                }
                Commands.ADD_TO_WISH_LIST -> {
                    addToWishList(payload)
                }
                Commands.CHECKOUT_START -> {
                    checkoutStart(payload)
                }
                Commands.SEARCH -> {
                    search(payload)
                }
                Commands.REGISTRATION_COMPLETE -> {
                    register(payload)
                }
                Commands.VIEW -> {
                    view(payload)
                }
                Commands.ACHIEVEMENT -> {
                    achieve(payload)
                }
                else -> {
                    logCustomEvent(command, payload)
                }
            }
        }
    }

    fun logCustomEvent(eventName: String, payload: JSONObject) {
        val standardParams = createStandardParams(payload)
        val parameters: JSONObject? = payload.optJSONObject(Parameters.CUSTOM_PARAMETERS)
        parameters?.let {
            val mergedParams = mergeJSONObjects(parameters, standardParams)
            tracker.customEvent(eventName, mergedParams.toString())
        } ?:run {
            tracker.customEvent(eventName, standardParams.toString())
        }
    }

    fun achieve(achievement: JSONObject) {
        val userId = achievement.optString(Parameters.USER_ID)
        val name = achievement.optString(Parameters.NAME)
        val duration = achievement.optDouble(Parameters.DURATION)

        val parameters: JSONObject? = achievement.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                standardParams.put(Parameters.DURATION, duration)
            }

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.ACHIEVEMENT, mergedParams.toString())
        } ?: run {
            tracker.achievement(userId, name, duration)
        }
    }

    fun view(view: JSONObject) {
        val userId = view.optString(Parameters.USER_ID)
        val name = view.optString(Parameters.NAME)
        val contentId = view.optString(Parameters.CONTENT_ID)
        val referralFrom = view.optString(Parameters.REFERRAL_FROM)

        val parameters: JSONObject? = view.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            standardParams.put(Parameters.CONTENT_ID, contentId)
            standardParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.VIEW, mergedParams.toString())
        } ?: run {
            tracker.view(userId, name, contentId, referralFrom)
        }
    }

    fun register(registration: JSONObject) {
        val userId = registration.optString(Parameters.USER_ID)
        val userName = registration.optString(Parameters.USER_NAME)
        val referralFrom = registration.optString(Parameters.REFERRAL_FROM)

        val parameters: JSONObject? = registration.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.USER_NAME, userName)
            standardParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.REGISTRATION_COMPLETE, mergedParams.toString())
        } ?: run {
            tracker.registrationComplete(userId, userName, referralFrom)
        }
    }

    fun search(search: JSONObject) {
        val uri = search.optString(Parameters.URI)
        val results = search.optString(Parameters.RESULTS)

        val parameters: JSONObject? = search.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.URI, uri)
            standardParams.put(Parameters.RESULTS, results)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.SEARCH, mergedParams.toString())
        } ?: run {
            tracker.search(uri, results)
        }
    }

    fun checkoutStart(checkoutStart: JSONObject) {
        val userId = checkoutStart.optString(Parameters.USER_ID)
        val name = checkoutStart.optString(Parameters.NAME)
        val contentId = checkoutStart.optString(Parameters.CONTENT_ID)
        val guestCheckout = checkoutStart.optString(Parameters.CHECKOUT_AS_GUEST)
        val currency = checkoutStart.optString(Parameters.CURRENCY)

        val parameters: JSONObject? = checkoutStart.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            standardParams.put(Parameters.CONTENT_ID, contentId)
            standardParams.put(Parameters.CHECKOUT_AS_GUEST, guestCheckout)
            standardParams.put(Parameters.CURRENCY, currency)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.CHECKOUT_START, mergedParams.toString())
        } ?: run {
            tracker.checkoutStart(userId, name, contentId, guestCheckout, currency)
        }
    }

    fun addToWishList(wishlist: JSONObject) {
        val userId = wishlist.optString(Parameters.USER_ID)
        val name = wishlist.optString(Parameters.NAME)
        val contentId = wishlist.optString(Parameters.CONTENT_ID)
        val referralFrom = wishlist.optString(Parameters.REFERRAL_FROM)

        val parameters: JSONObject? = wishlist.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            standardParams.put(Parameters.CONTENT_ID, contentId)
            standardParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.ADD_TO_WISH_LIST, mergedParams.toString())
        } ?: run {
            tracker.addToWishList(userId, name, contentId, referralFrom)
        }
    }

    fun addToCart(cart: JSONObject) {
        val userId = cart.optString(Parameters.USER_ID)
        val name = cart.optString(Parameters.NAME)
        val contentId = cart.optString(Parameters.CONTENT_ID)
        val quantity = cart.optDouble(Parameters.QUANTITY)
        val referralFrom = cart.optString(Parameters.REFERRAL_FROM)

        val parameters: JSONObject? = cart.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            standardParams.put(Parameters.CONTENT_ID, contentId)
            if (!quantity.isNaN()) {
                standardParams.put(Parameters.QUANTITY, quantity)
            }
            standardParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.ADD_TO_CART, mergedParams.toString())
        } ?: run {
            tracker.addToCart(userId, name, contentId, quantity, referralFrom)
        }
    }

    fun rating(rating: JSONObject) {
        val value = rating.optDouble(Parameters.RATING_VALUE)
        val maxRating = rating.optDouble(Parameters.MAX_RATING_VALUE)

        val parameters: JSONObject? = rating.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            if (!value.isNaN()) {
                standardParams.put(Parameters.RATING_VALUE, value)
            }
            if (!maxRating.isNaN()) {
                standardParams.put(Parameters.MAX_RATING_VALUE, maxRating)
            }

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.RATING, mergedParams.toString())
        } ?: run {
            tracker.rating(value, maxRating)
        }
    }

    fun adView(adview: JSONObject) {
        val type = adview.optString(Parameters.AD_TYPE)
        val networkName = adview.optString(Parameters.AD_NETWORK_NAME)
        val placement = adview.optString(Parameters.PLACEMENT)
        val mediationName = adview.optString(Parameters.AD_MEDIATION_NAME)
        val campaignId = adview.optString(Parameters.AD_CAMPAIGN_ID)
        val campaignName = adview.optString(Parameters.AD_CAMPAIGN_NAME)
        val size = adview.optString(Parameters.AD_SIZE)

        val parameters: JSONObject? = adview.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, type)
            standardParams.put(Parameters.NAME, networkName)
            standardParams.put(Parameters.DURATION, placement)
            standardParams.put(Parameters.USER_ID, mediationName)
            standardParams.put(Parameters.NAME, campaignId)
            standardParams.put(Parameters.DURATION, campaignName)

            val mergedParams = mergeJSONObjects(standardParams, parameters)
            tracker.customEvent(Commands.AD_VIEW, mergedParams.toString())
        } ?: run {
            tracker.adView(
                type,
                networkName,
                placement,
                mediationName,
                campaignId,
                campaignName,
                size
            )
        }
    }

    fun tutorialComplete(tutorial: JSONObject) {
        val userId = tutorial.optString(Parameters.USER_ID)
        val name = tutorial.optString(Parameters.NAME)
        val duration = tutorial.optDouble(Parameters.DURATION) as Double

        val parameters: JSONObject? = tutorial.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                standardParams.put(Parameters.DURATION, duration)
            }

            val mergedParams = mergeJSONObjects(standardParams, parameters)

            tracker.customEvent(Commands.TUTORIAL_COMPLETE, mergedParams.toString())
        } ?: run {
            tracker.tutorialLevelComplete(Commands.TUTORIAL_COMPLETE, userId, name, duration)
        }
    }

    fun levelComplete(level: JSONObject) {
        val userId = level.optString(Parameters.USER_ID)
        val name = level.optString(Parameters.NAME)
        val duration = level.optDouble(Parameters.DURATION) as Double

        val parameters: JSONObject? = level.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                standardParams.put(Parameters.DURATION, duration)
            }
            val mergedParams = mergeJSONObjects(standardParams, parameters)

            tracker.customEvent(Commands.LEVEL_COMPLETE, mergedParams.toString())
        } ?: run {
            tracker.tutorialLevelComplete(Commands.LEVEL_COMPLETE, userId, name, duration)
        }
    }

    fun purchase(purchase: JSONObject) {
        val userId = purchase.optString(Parameters.USER_ID)
        val name = purchase.optString(Parameters.NAME)
        val contentId = purchase.optString(Parameters.CONTENT_ID)
        val price = purchase.optDouble(Parameters.PRICE) as Double
        val currency = purchase.optString(Parameters.CURRENCY)
        val guestCheckout = purchase.optString(Parameters.CHECKOUT_AS_GUEST)
        val parameters: JSONObject? = purchase.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val standardParams = JSONObject()
            standardParams.put(Parameters.USER_ID, userId)
            standardParams.put(Parameters.NAME, name)
            standardParams.put(Parameters.CONTENT_ID, contentId)
            if (!price.isNaN()) {
                standardParams.put(Parameters.PRICE, price)
            }
            standardParams.put(Parameters.CURRENCY, currency)
            standardParams.put(Parameters.CHECKOUT_AS_GUEST, guestCheckout)
            val mergedParams = mergeJSONObjects(standardParams, parameters)

            tracker.customEvent(Commands.PURCHASE, mergedParams.toString())
        } ?: run {
            tracker.purchase(userId, name, contentId, price, currency, guestCheckout)
        }

    }

    fun mergeJSONObjects(json1: JSONObject, json2: JSONObject): JSONObject {
        val iter1 = json1.keys()
        val iter2 = json2.keys()
        val merged = JSONObject()
        while (iter1.hasNext()) {
            val key = iter1.next()
            if(json1.get(key) != "") {
                merged.put(key, json1.get(key))
            }
        }

        while (iter2.hasNext()) {
            val key = iter2.next()
            if (json2.get(key) != "") {
                merged.put(key, json2.get(key))
            }
        }
        return merged
    }

    fun createStandardParams(payload: JSONObject): JSONObject {
        val standardParams = JSONObject()

        val deviceType = payload.optString(Parameters.DEVICE_TYPE)
        val contentType = payload.optString(Parameters.CONTENT_TYPE)
        val contentId = payload.optString(Parameters.CONTENT_ID)
        val guestCheckout = payload.optString(Parameters.CHECKOUT_AS_GUEST)
        val adMediationName = payload.optString(Parameters.AD_MEDIATION_NAME)
        val adNetworkName = payload.optString(Parameters.AD_NETWORK_NAME)
        val adGroupId = payload.optString(Parameters.AD_GROUP_ID)
        val adGroupName = payload.optString(Parameters.AD_GROUP_NAME)
        val adSize = payload.optString(Parameters.AD_SIZE)
        val adCampaignName = payload.optString(Parameters.AD_CAMPAIGN_NAME)
        val adCampaignId = payload.optString(Parameters.AD_CAMPAIGN_ID)
        val adType = payload.optString(Parameters.AD_TYPE)
        val placement = payload.optString(Parameters.PLACEMENT)
        val endDate = payload.optString(Parameters.END_DATE)
        val duration = payload.optDouble(Parameters.DURATION)
        val destination = payload.optString(Parameters.DESTINATION)
        val description = payload.optString(Parameters.DESCRIPTION)
        val date = payload.optString(Parameters.DATE)
        val currency = payload.optString(Parameters.CURRENCY)
        val origin = payload.optString(Parameters.ORIGIN)
        val orderId = payload.optString(Parameters.ORDER_ID)
        val name = payload.optString(Parameters.NAME)
        val maxRating = payload.optDouble(Parameters.MAX_RATING_VALUE)
        val level = payload.optString(Parameters.LEVEL)
        val itemAddedFrom = payload.optString(Parameters.ITEM_ADDED_FROM)
        val source = payload.optString(Parameters.SOURCE)
        val uri = payload.optString(Parameters.URI)
        val completed = payload.optString(Parameters.COMPLETED)
        val action = payload.optString(Parameters.ACTION)
        val background = payload.optString(Parameters.BACKGROUND)
        val spatialX = payload.optDouble(Parameters.SPATIAL_X)
        val spatialY = payload.optDouble(Parameters.SPATIAL_Y)
        val spatialZ = payload.optDouble(Parameters.SPATIAL_Z)
        val validated = payload.optString(Parameters.VALIDATED)
        val userName = payload.optString(Parameters.USER_NAME)
        val userId = payload.optString(Parameters.USER_ID)
        val success = payload.optString(Parameters.SUCCESS)
        val startDate = payload.optString(Parameters.START_DATE)
        val searchTerm = payload.optString(Parameters.SEARCH_TERM)
        val score = payload.optString(Parameters.SCORE)
        val results = payload.optString(Parameters.RESULTS)
        val registrationMethod = payload.optString(Parameters.REGISTRATION_METHOD)
        val referralFrom = payload.optString(Parameters.REFERRAL_FROM)
        val receiptId = payload.optString(Parameters.RECEIPT_ID)
        val ratingValue = payload.optDouble(Parameters.RATING_VALUE)
        val quantity = payload.optDouble(Parameters.QUANTITY)
        val price = payload.optDouble(Parameters.PRICE)

        if (adType.isNotEmpty()) {
            standardParams.put(Parameters.AD_TYPE, adType)
        }
        if (adCampaignId.isNotEmpty()) {
            standardParams.put(Parameters.AD_CAMPAIGN_ID, adCampaignId)
        }
        if (adCampaignName.isNotEmpty()) {
            standardParams.put(Parameters.AD_CAMPAIGN_NAME, adCampaignName)
        }
        if (adSize.isNotEmpty()) {
            standardParams.put(Parameters.AD_SIZE, adSize)
        }
        if (adGroupName.isNotEmpty()) {
            standardParams.put(Parameters.AD_GROUP_NAME, adGroupName)
        }
        if (adGroupId.isNotEmpty()) {
            standardParams.put(Parameters.AD_GROUP_ID, adGroupId)
        }
        if (adNetworkName.isNotEmpty()) {
            standardParams.put(Parameters.AD_NETWORK_NAME, adNetworkName)
        }
        if (adMediationName.isNotEmpty()) {
            standardParams.put(Parameters.AD_MEDIATION_NAME, adMediationName)
        }
        if (contentId.isNotEmpty()) {
            standardParams.put(Parameters.CONTENT_ID, contentId)
        }
        if (contentType.isNotEmpty()) {
            standardParams.put(Parameters.CONTENT_TYPE, contentType)
        }
        if (deviceType.isNotEmpty()) {
            standardParams.put(Parameters.DEVICE_TYPE, deviceType)
        }
        if (date.isNotEmpty()) {
            standardParams.put(Parameters.DATE, date)
        }
        if (description.isNotEmpty()) {
            standardParams.put(Parameters.DESCRIPTION, description)
        }
        if (destination.isNotEmpty()) {
            standardParams.put(Parameters.DESTINATION, destination)
        }
        if (!duration.isNaN()) {
            standardParams.put(Parameters.DURATION, duration)
        }
        if (endDate.isNotEmpty()) {
            standardParams.put(Parameters.END_DATE, endDate)
        }
        if (placement.isNotEmpty()) {
            standardParams.put(Parameters.PLACEMENT, placement)
        }
        if (level.isNotEmpty()) {
            standardParams.put(Parameters.LEVEL, level)
        }
        if (!maxRating.isNaN()) {
            standardParams.put(Parameters.MAX_RATING_VALUE, maxRating)
        }
        if (orderId.isNotEmpty()) {
            standardParams.put(Parameters.ORDER_ID, orderId)
        }
        if (origin.isNotEmpty()) {
            standardParams.put(Parameters.ORIGIN, origin)
        }
        if (currency.isNotEmpty()) {
            standardParams.put(Parameters.CURRENCY, currency)
        }
        if (results.isNotEmpty()) {
            standardParams.put(Parameters.RESULTS, results)
        }
        if (score.isNotEmpty()) {
            standardParams.put(Parameters.SCORE, score)
        }
        if (searchTerm.isNotEmpty()) {
            standardParams.put(Parameters.SEARCH_TERM, searchTerm)
        }
        if (startDate.isNotEmpty()) {
            standardParams.put(Parameters.START_DATE, startDate)
        }
        if (success.isNotEmpty()) {
            standardParams.put(Parameters.SUCCESS, success)
        }
        if (userName.isNotEmpty()) {
            standardParams.put(Parameters.USER_NAME, userName)
        }
        if (validated.isNotEmpty()) {
            standardParams.put(Parameters.VALIDATED, validated)
        }
        if (!spatialX.isNaN()) {
            standardParams.put(Parameters.SPATIAL_X, spatialX)
        }
        if (!spatialY.isNaN()) {
            standardParams.put(Parameters.SPATIAL_Y, spatialY)
        }
        if (!spatialZ.isNaN()) {
            standardParams.put(Parameters.SPATIAL_Z, spatialZ)
        }
        if (background.toLowerCase() == "true" || background.toLowerCase() == "false") {
            standardParams.put(Parameters.BACKGROUND, background.toBoolean())
        }
        if (action.isNotEmpty()) {
            standardParams.put(Parameters.ACTION, action)
        }
        if (completed.toLowerCase() == "true" || completed.toLowerCase() == "false") {
            standardParams.put(Parameters.COMPLETED, completed.toBoolean())
        }
        if (receiptId.isNotEmpty()) {
            standardParams.put(Parameters.RECEIPT_ID, receiptId)
        }
        if (referralFrom.isNotEmpty()) {
            standardParams.put(Parameters.REFERRAL_FROM, referralFrom)
        }
        if (registrationMethod.isNotEmpty()) {
            standardParams.put(Parameters.REGISTRATION_METHOD, registrationMethod)
        }
        if (uri.isNotEmpty()) {
            standardParams.put(Parameters.URI, uri)
        }
        if (source.isNotEmpty()) {
            standardParams.put(Parameters.SOURCE, source)
        }
        if (itemAddedFrom.isNotEmpty()) {
            standardParams.put(Parameters.ITEM_ADDED_FROM, itemAddedFrom)
        }
        if (userId.isNotEmpty()) {
            standardParams.put(Parameters.USER_ID, userId)
        }
        if (name.isNotEmpty()) {
            standardParams.put(Parameters.NAME, name)
        }
        if (!price.isNaN()) {
            standardParams.put(Parameters.PRICE, price)
        }
        if (!quantity.isNaN()) {
            standardParams.put(Parameters.QUANTITY, quantity)
        }
        if (!ratingValue.isNaN()) {
            standardParams.put(Parameters.RATING_VALUE, ratingValue)
        }
        if (guestCheckout.isNotEmpty()) {
            standardParams.put(Parameters.CHECKOUT_AS_GUEST, guestCheckout)
        }

        return standardParams
    }

}