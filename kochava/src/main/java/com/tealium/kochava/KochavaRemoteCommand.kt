package com.tealium.kochava

import Commands
import Parameters
import android.app.Application
import com.tealium.internal.tagbridge.RemoteCommand
import org.json.JSONObject

class KochavaRemoteCommand : RemoteCommand {

    lateinit var tracker: KochavaTrackable
    var application: Application? = null

    /**
     * Constructs a RemoteCommand that integrates with the Facebook App Events SDK to allow Facebook API calls to be implemented through Tealium.
     */
    @JvmOverloads
    constructor(
        application: Application? = null,
        appGuid: String,
        commandId: String = DEFAULT_COMMAND_ID,
        description: String = DEFAULT_COMMAND_DESCRIPTION
    ) : super(commandId, description) {
        application?.let {
            tracker = KochavaTracker(
                application,
                appGuid
            )
        }
    }

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
        val eventParams = payload.optJSONObject(Parameters.EVENT_PARAMS)
        commands.forEach { command ->
            when (command) {
                Commands.CONFIGURE -> {
                    initialize(payload)
                }
                Commands.PURCHASE -> {
                    purchase(eventParams)
                }
                Commands.TUTORIAL_COMPLETE -> {
                    tutorialComplete(eventParams)
                }
                Commands.LEVEL_COMPLETE -> {
                    levelComplete(eventParams)
                }
                Commands.AD_VIEW -> {
                    adView(eventParams)
                }
                Commands.RATING -> {
                    rating(eventParams)
                }
                Commands.ADD_TO_CART -> {
                    addToCart(eventParams)
                }
                Commands.ADD_TO_WISH_LIST -> {
                    addToWishList(eventParams)
                }
                Commands.CHECKOUT_START -> {
                    checkoutStart(eventParams)
                }
                Commands.SEARCH -> {
                    search(eventParams)
                }
                Commands.REGISTRATION_COMPLETE -> {
                    register(eventParams)
                }
                Commands.VIEW -> {
                    view(eventParams)
                }
                Commands.ACHIEVEMENT -> {
                    achieve(eventParams)
                }
                Commands.DEEP_LINK -> {
                    deepLink(eventParams)
                }
                Commands.SUBSCRIBE -> {
                    subscribe(eventParams)
                }
                Commands.START_TRIAL -> {
                    startTrial(eventParams)
                }
                else -> {
                    logCustomEvent(command, eventParams)
                }
            }
        }
    }

    private fun initialize(payload: JSONObject) {
        val configurationParams = payload.optJSONObject(Parameters.CONFIGURATION_PARAMS)
        application?.let {
            tracker.configure(it, configurationParams)
        }
    }

    private fun logCustomEvent(eventName: String, payload: JSONObject) {
        val standardParams = createStandardParams(payload)
        val parameters: JSONObject? = payload.optJSONObject(EventParameters.CUSTOM_PARAMETERS)
        parameters?.let {
            val mergedParams = mergeJSONObjects(parameters, standardParams)

        } ?: run {
            tracker.customEvent(eventName, payload.toString())
        }
    }

    private fun subscribe(subscribe: JSONObject) {
        val price = subscribe.optDouble(EventParameters.PRICE)
        val currency = subscribe.optString(EventParameters.CURRENCY)
        val name = subscribe.optString(EventParameters.NAME)
        val userId = subscribe.optString(EventParameters.USER_ID)

        val parameters: JSONObject? = subscribe.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.CURRENCY, currency)
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            if (!price.isNaN()) {
                it.put(EventParameters.PRICE, price)
            }

            tracker.customEvent(Commands.SUBSCRIBE, it.toString())
        } ?: run {
            tracker.subscribe(price, currency, name, userId)
        }
    }

    private fun startTrial(trial: JSONObject) {
        val price = trial.optDouble(EventParameters.PRICE)
        val currency = trial.optString(EventParameters.CURRENCY)
        val name = trial.optString(EventParameters.NAME)
        val userId = trial.optString(EventParameters.USER_ID)

        val parameters: JSONObject? = trial.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.CURRENCY, currency)
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            if (!price.isNaN()) {
                it.put(EventParameters.PRICE, price)
            }

            tracker.customEvent(Commands.START_TRIAL, it.toString())
        } ?: run {
            tracker.startTrial(price, currency, name, userId)
        }
    }

    private fun deepLink(deepLink: JSONObject) {
        val uri = deepLink.optString(EventParameters.URI)

        val parameters: JSONObject? = deepLink.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.URI, uri)

            tracker.customEvent(Commands.DEEP_LINK, it.toString())
        } ?: run {
            tracker.deepLink(uri)
        }
    }

    private fun achieve(achievement: JSONObject) {
        val userId = achievement.optString(EventParameters.USER_ID)
        val name = achievement.optString(EventParameters.NAME)
        val duration = achievement.optDouble(EventParameters.DURATION)

        val parameters: JSONObject? = achievement.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            if (!duration.isNaN()) {
                it.put(EventParameters.DURATION, duration)
            }

            tracker.customEvent(Commands.ACHIEVEMENT, it.toString())
        } ?: run {
            tracker.achievement(userId, name, duration)
        }
    }

    private fun view(view: JSONObject) {
        val userId = view.optString(EventParameters.USER_ID)
        val name = view.optString(EventParameters.NAME)
        val contentId = view.optString(EventParameters.CONTENT_ID)
        val referralFrom = view.optString(EventParameters.REFERRAL_FROM)

        val parameters: JSONObject? = view.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            it.put(EventParameters.CONTENT_ID, contentId)
            it.put(EventParameters.REFERRAL_FROM, referralFrom)

            tracker.customEvent(Commands.VIEW, it.toString())
        } ?: run {
            tracker.view(userId, name, contentId, referralFrom)
        }
    }

    private fun register(registration: JSONObject) {
        val userId = registration.optString(EventParameters.USER_ID)
        val userName = registration.optString(EventParameters.USER_NAME)
        val referralFrom = registration.optString(EventParameters.REFERRAL_FROM)

        val parameters: JSONObject? = registration.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.USER_NAME, userName)
            it.put(EventParameters.REFERRAL_FROM, referralFrom)

            tracker.customEvent(Commands.REGISTRATION_COMPLETE, it.toString())
        } ?: run {
            tracker.registrationComplete(userId, userName, referralFrom)
        }
    }

    private fun search(search: JSONObject) {
        val uri = search.optString(EventParameters.URI)
        val results = search.optString(EventParameters.RESULTS)

        val parameters: JSONObject? = search.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.URI, uri)
            it.put(EventParameters.RESULTS, results)

            tracker.customEvent(Commands.SEARCH, it.toString())
        } ?: run {
            tracker.search(uri, results)
        }
    }

    private fun checkoutStart(checkoutStart: JSONObject) {
        val userId = checkoutStart.optString(EventParameters.USER_ID)
        val name = checkoutStart.optString(EventParameters.NAME)
        val contentId = checkoutStart.optString(EventParameters.CONTENT_ID)
        val guestCheckout = checkoutStart.optString(EventParameters.CHECKOUT_AS_GUEST)
        val currency = checkoutStart.optString(EventParameters.CURRENCY)

        val parameters: JSONObject? = checkoutStart.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            it.put(EventParameters.CONTENT_ID, contentId)
            it.put(EventParameters.CHECKOUT_AS_GUEST, guestCheckout)
            it.put(EventParameters.CURRENCY, currency)

            tracker.customEvent(Commands.CHECKOUT_START, it.toString())
        } ?: run {
            tracker.checkoutStart(userId, name, contentId, guestCheckout, currency)
        }
    }

    private fun addToWishList(wishlist: JSONObject) {
        val userId = wishlist.optString(EventParameters.USER_ID)
        val name = wishlist.optString(EventParameters.NAME)
        val contentId = wishlist.optString(EventParameters.CONTENT_ID)
        val referralFrom = wishlist.optString(EventParameters.REFERRAL_FROM)

        val parameters: JSONObject? = wishlist.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            it.put(EventParameters.CONTENT_ID, contentId)
            it.put(EventParameters.REFERRAL_FROM, referralFrom)

            tracker.customEvent(Commands.ADD_TO_WISH_LIST, it.toString())
        } ?: run {
            tracker.addToWishList(userId, name, contentId, referralFrom)
        }
    }

    private fun addToCart(cart: JSONObject) {
        val userId = cart.optString(EventParameters.USER_ID)
        val name = cart.optString(EventParameters.NAME)
        val contentId = cart.optString(EventParameters.CONTENT_ID)
        val quantity = cart.optDouble(EventParameters.QUANTITY)
        val referralFrom = cart.optString(EventParameters.REFERRAL_FROM)

        val parameters: JSONObject? = cart.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            it.put(EventParameters.CONTENT_ID, contentId)
            if (!quantity.isNaN()) {
                it.put(EventParameters.QUANTITY, quantity)
            }
            it.put(EventParameters.REFERRAL_FROM, referralFrom)

            tracker.customEvent(Commands.ADD_TO_CART, it.toString())
        } ?: run {
            tracker.addToCart(userId, name, contentId, quantity, referralFrom)
        }
    }

    private fun rating(rating: JSONObject) {
        val value = rating.optDouble(EventParameters.RATING_VALUE)
        val maxRating = rating.optDouble(EventParameters.MAX_RATING_VALUE)

        val parameters: JSONObject? = rating.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            if (!value.isNaN()) {
                it.put(EventParameters.RATING_VALUE, value)
            }
            if (!maxRating.isNaN()) {
                it.put(EventParameters.MAX_RATING_VALUE, maxRating)
            }

            tracker.customEvent(Commands.RATING, it.toString())
        } ?: run {
            tracker.rating(value, maxRating)
        }
    }

    private fun adView(adview: JSONObject) {
        val type = adview.optString(EventParameters.AD_TYPE)
        val networkName = adview.optString(EventParameters.AD_NETWORK_NAME)
        val placement = adview.optString(EventParameters.PLACEMENT)
        val mediationName = adview.optString(EventParameters.AD_MEDIATION_NAME)
        val campaignId = adview.optString(EventParameters.AD_CAMPAIGN_ID)
        val campaignName = adview.optString(EventParameters.AD_CAMPAIGN_NAME)
        val size = adview.optString(EventParameters.AD_SIZE)

        val parameters: JSONObject? = adview.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, type)
            it.put(EventParameters.NAME, networkName)
            it.put(EventParameters.DURATION, placement)
            it.put(EventParameters.USER_ID, mediationName)
            it.put(EventParameters.NAME, campaignId)
            it.put(EventParameters.DURATION, campaignName)

            tracker.customEvent(Commands.AD_VIEW, it.toString())
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

    private fun tutorialComplete(tutorial: JSONObject) {
        val userId = tutorial.optString(EventParameters.USER_ID)
        val name = tutorial.optString(EventParameters.NAME)
        val duration = tutorial.optDouble(EventParameters.DURATION) as Double

        val parameters: JSONObject? = tutorial.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            if (!duration.isNaN()) {
                it.put(EventParameters.DURATION, duration)
            }

            tracker.customEvent(Commands.TUTORIAL_COMPLETE, it.toString())
        } ?: run {
            tracker.tutorialComplete(userId, name, duration)
        }
    }

    private fun levelComplete(level: JSONObject) {
        val userId = level.optString(EventParameters.USER_ID)
        val name = level.optString(EventParameters.NAME)
        val duration = level.optDouble(EventParameters.DURATION) as Double

        val parameters: JSONObject? = level.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            if (!duration.isNaN()) {
                it.put(EventParameters.DURATION, duration)
            }

            tracker.customEvent(Commands.LEVEL_COMPLETE, it.toString())
        } ?: run {
            tracker.levelComplete(userId, name, duration)
        }
    }

    private fun purchase(purchase: JSONObject) {
        val userId = purchase.optString(EventParameters.USER_ID)
        val name = purchase.optString(EventParameters.NAME)
        val contentId = purchase.optString(EventParameters.CONTENT_ID)
        val price = purchase.optDouble(EventParameters.PRICE) as Double
        val currency = purchase.optString(EventParameters.CURRENCY)
        val guestCheckout = purchase.optString(EventParameters.CHECKOUT_AS_GUEST)
        val parameters: JSONObject? = purchase.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            it.put(EventParameters.USER_ID, userId)
            it.put(EventParameters.NAME, name)
            it.put(EventParameters.CONTENT_ID, contentId)
            if (!price.isNaN()) {
                it.put(EventParameters.PRICE, price)
            }
            it.put(EventParameters.CURRENCY, currency)
            it.put(EventParameters.CHECKOUT_AS_GUEST, guestCheckout)

            tracker.customEvent(Commands.PURCHASE, it.toString())
        } ?: run {
            tracker.purchase(userId, name, contentId, price, currency, guestCheckout)
        }

    }

    private fun mergeJSONObjects(json1: JSONObject, json2: JSONObject): JSONObject {
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

    private fun createStandardParams(payload: JSONObject): JSONObject {
        val standardParams = JSONObject()

        val deviceType = payload.optString(EventParameters.DEVICE_TYPE)
        val contentType = payload.optString(EventParameters.CONTENT_TYPE)
        val contentId = payload.optString(EventParameters.CONTENT_ID)
        val guestCheckout = payload.optString(EventParameters.CHECKOUT_AS_GUEST)
        val adMediationName = payload.optString(EventParameters.AD_MEDIATION_NAME)
        val adNetworkName = payload.optString(EventParameters.AD_NETWORK_NAME)
        val adGroupId = payload.optString(EventParameters.AD_GROUP_ID)
        val adGroupName = payload.optString(EventParameters.AD_GROUP_NAME)
        val adSize = payload.optString(EventParameters.AD_SIZE)
        val adCampaignName = payload.optString(EventParameters.AD_CAMPAIGN_NAME)
        val adCampaignId = payload.optString(EventParameters.AD_CAMPAIGN_ID)
        val adType = payload.optString(EventParameters.AD_TYPE)
        val placement = payload.optString(EventParameters.PLACEMENT)
        val endDate = payload.optString(EventParameters.END_DATE)
        val duration = payload.optDouble(EventParameters.DURATION)
        val destination = payload.optString(EventParameters.DESTINATION)
        val description = payload.optString(EventParameters.DESCRIPTION)
        val date = payload.optString(EventParameters.DATE)
        val currency = payload.optString(EventParameters.CURRENCY)
        val origin = payload.optString(EventParameters.ORIGIN)
        val orderId = payload.optString(EventParameters.ORDER_ID)
        val name = payload.optString(EventParameters.NAME)
        val maxRating = payload.optDouble(EventParameters.MAX_RATING_VALUE)
        val level = payload.optString(EventParameters.LEVEL)
        val itemAddedFrom = payload.optString(EventParameters.ITEM_ADDED_FROM)
        val source = payload.optString(EventParameters.SOURCE)
        val uri = payload.optString(EventParameters.URI)
        val completed = payload.optString(EventParameters.COMPLETED)
        val action = payload.optString(EventParameters.ACTION)
        val background = payload.optString(EventParameters.BACKGROUND)
        val spatialX = payload.optDouble(EventParameters.SPATIAL_X)
        val spatialY = payload.optDouble(EventParameters.SPATIAL_Y)
        val spatialZ = payload.optDouble(EventParameters.SPATIAL_Z)
        val validated = payload.optString(EventParameters.VALIDATED)
        val userName = payload.optString(EventParameters.USER_NAME)
        val userId = payload.optString(EventParameters.USER_ID)
        val success = payload.optString(EventParameters.SUCCESS)
        val startDate = payload.optString(EventParameters.START_DATE)
        val searchTerm = payload.optString(EventParameters.SEARCH_TERM)
        val score = payload.optString(EventParameters.SCORE)
        val results = payload.optString(EventParameters.RESULTS)
        val registrationMethod = payload.optString(EventParameters.REGISTRATION_METHOD)
        val referralFrom = payload.optString(EventParameters.REFERRAL_FROM)
        val receiptId = payload.optString(EventParameters.RECEIPT_ID)
        val ratingValue = payload.optDouble(EventParameters.RATING_VALUE)
        val quantity = payload.optDouble(EventParameters.QUANTITY)
        val price = payload.optDouble(EventParameters.PRICE)

        if (adType.isNotEmpty()) {
            standardParams.put(EventParameters.AD_TYPE, adType)
        }
        if (adCampaignId.isNotEmpty()) {
            standardParams.put(EventParameters.AD_CAMPAIGN_ID, adCampaignId)
        }
        if (adCampaignName.isNotEmpty()) {
            standardParams.put(EventParameters.AD_CAMPAIGN_NAME, adCampaignName)
        }
        if (adSize.isNotEmpty()) {
            standardParams.put(EventParameters.AD_SIZE, adSize)
        }
        if (adGroupName.isNotEmpty()) {
            standardParams.put(EventParameters.AD_GROUP_NAME, adGroupName)
        }
        if (adGroupId.isNotEmpty()) {
            standardParams.put(EventParameters.AD_GROUP_ID, adGroupId)
        }
        if (adNetworkName.isNotEmpty()) {
            standardParams.put(EventParameters.AD_NETWORK_NAME, adNetworkName)
        }
        if (adMediationName.isNotEmpty()) {
            standardParams.put(EventParameters.AD_MEDIATION_NAME, adMediationName)
        }
        if (contentId.isNotEmpty()) {
            standardParams.put(EventParameters.CONTENT_ID, contentId)
        }
        if (contentType.isNotEmpty()) {
            standardParams.put(EventParameters.CONTENT_TYPE, contentType)
        }
        if (deviceType.isNotEmpty()) {
            standardParams.put(EventParameters.DEVICE_TYPE, deviceType)
        }
        if (date.isNotEmpty()) {
            standardParams.put(EventParameters.DATE, date)
        }
        if (description.isNotEmpty()) {
            standardParams.put(EventParameters.DESCRIPTION, description)
        }
        if (destination.isNotEmpty()) {
            standardParams.put(EventParameters.DESTINATION, destination)
        }
        if (!duration.isNaN()) {
            standardParams.put(EventParameters.DURATION, duration)
        }
        if (endDate.isNotEmpty()) {
            standardParams.put(EventParameters.END_DATE, endDate)
        }
        if (placement.isNotEmpty()) {
            standardParams.put(EventParameters.PLACEMENT, placement)
        }
        if (level.isNotEmpty()) {
            standardParams.put(EventParameters.LEVEL, level)
        }
        if (!maxRating.isNaN()) {
            standardParams.put(EventParameters.MAX_RATING_VALUE, maxRating)
        }
        if (orderId.isNotEmpty()) {
            standardParams.put(EventParameters.ORDER_ID, orderId)
        }
        if (origin.isNotEmpty()) {
            standardParams.put(EventParameters.ORIGIN, origin)
        }
        if (currency.isNotEmpty()) {
            standardParams.put(EventParameters.CURRENCY, currency)
        }
        if (results.isNotEmpty()) {
            standardParams.put(EventParameters.RESULTS, results)
        }
        if (score.isNotEmpty()) {
            standardParams.put(EventParameters.SCORE, score)
        }
        if (searchTerm.isNotEmpty()) {
            standardParams.put(EventParameters.SEARCH_TERM, searchTerm)
        }
        if (startDate.isNotEmpty()) {
            standardParams.put(EventParameters.START_DATE, startDate)
        }
        if (success.isNotEmpty()) {
            standardParams.put(EventParameters.SUCCESS, success)
        }
        if (userName.isNotEmpty()) {
            standardParams.put(EventParameters.USER_NAME, userName)
        }
        if (validated.isNotEmpty()) {
            standardParams.put(EventParameters.VALIDATED, validated)
        }
        if (!spatialX.isNaN()) {
            standardParams.put(EventParameters.SPATIAL_X, spatialX)
        }
        if (!spatialY.isNaN()) {
            standardParams.put(EventParameters.SPATIAL_Y, spatialY)
        }
        if (!spatialZ.isNaN()) {
            standardParams.put(EventParameters.SPATIAL_Z, spatialZ)
        }
        if (background.toLowerCase() == "true" || background.toLowerCase() == "false") {
            standardParams.put(EventParameters.BACKGROUND, background.toBoolean())
        }
        if (action.isNotEmpty()) {
            standardParams.put(EventParameters.ACTION, action)
        }
        if (completed.toLowerCase() == "true" || completed.toLowerCase() == "false") {
            standardParams.put(EventParameters.COMPLETED, completed.toBoolean())
        }
        if (receiptId.isNotEmpty()) {
            standardParams.put(EventParameters.RECEIPT_ID, receiptId)
        }
        if (referralFrom.isNotEmpty()) {
            standardParams.put(EventParameters.REFERRAL_FROM, referralFrom)
        }
        if (registrationMethod.isNotEmpty()) {
            standardParams.put(EventParameters.REGISTRATION_METHOD, registrationMethod)
        }
        if (uri.isNotEmpty()) {
            standardParams.put(EventParameters.URI, uri)
        }
        if (source.isNotEmpty()) {
            standardParams.put(EventParameters.SOURCE, source)
        }
        if (itemAddedFrom.isNotEmpty()) {
            standardParams.put(EventParameters.ITEM_ADDED_FROM, itemAddedFrom)
        }
        if (userId.isNotEmpty()) {
            standardParams.put(EventParameters.USER_ID, userId)
        }
        if (name.isNotEmpty()) {
            standardParams.put(EventParameters.NAME, name)
        }
        if (!price.isNaN()) {
            standardParams.put(EventParameters.PRICE, price)
        }
        if (!quantity.isNaN()) {
            standardParams.put(EventParameters.QUANTITY, quantity)
        }
        if (!ratingValue.isNaN()) {
            standardParams.put(EventParameters.RATING_VALUE, ratingValue)
        }
        if (guestCheckout.isNotEmpty()) {
            standardParams.put(EventParameters.CHECKOUT_AS_GUEST, guestCheckout)
        }

        return standardParams
    }

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }

}