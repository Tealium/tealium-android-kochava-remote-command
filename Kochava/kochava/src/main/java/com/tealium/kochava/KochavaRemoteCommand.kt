package com.tealium.kochava

import android.app.Application
import com.tealium.internal.tagbridge.RemoteCommand
import org.json.JSONObject


open class KochavaRemoteCommand : RemoteCommand {

    private val TAG = this::class.java.simpleName

    var tracker: KochavaTrackable
    /**
     * Handles the incoming RemoteCommand response data. Prepares the command and payload for parsing.
     *
     * @param response - the response that includes the command and optional command parameters
     */
    @Throws(Exception::class)
    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        println("payload " + payload)
        parseCommands(commands, payload)
    }

    @JvmOverloads
    constructor(
        application: Application,
        appGuid: String,
        commandId: String = DEFAULT_COMMAND_ID,
        description: String = DEFAULT_COMMAND_DESCRIPTION,
        tracker: KochavaTrackable = KochavaTracker(application, appGuid)
    ) : super(commandId, description) {
        this.tracker = tracker
    }

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
        val REQUIRED_KEY = "key does not exist in the payload."
    }

    fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(Commands.COMMAND_KEY)
        return command.split(Commands.SEPARATOR.toRegex())
            .dropLastWhile {
                it.isEmpty()
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
//        val eventParams = createStandardParams(payload)
//        val mergedParams = mergeJSONObjects(payload, eventParams)
        tracker.customEvent(eventName, payload.toString())
    }

    fun achieve(achievement: JSONObject) {
        val userId = achievement.optString(Parameters.USER_ID)
        val name = achievement.optString(Parameters.NAME)
        val duration = achievement.optDouble(Parameters.DURATION) as Double

        val parameters: JSONObject? = achievement.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                stdParams.put(Parameters.DURATION, duration)
            }

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Achievement", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.NAME, name)
            stdParams.put(Parameters.CONTENT_ID, contentId)
            stdParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("View", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.USER_NAME, userName)
            stdParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Registration Complete", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.URI, uri)
            stdParams.put(Parameters.RESULTS, results)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Search", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.NAME, name)
            stdParams.put(Parameters.CONTENT_ID, contentId)
            stdParams.put(Parameters.CHECKOUT_AS_GUEST, guestCheckout)
            stdParams.put(Parameters.CURRENCY, currency)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Checkout Start", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.NAME, name)
            stdParams.put(Parameters.CONTENT_ID, contentId)
            stdParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Add To Wishlist", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userId)
            stdParams.put(Parameters.NAME, name)
            stdParams.put(Parameters.CONTENT_ID, contentId)
            if (!quantity.isNaN()) {
                stdParams.put(Parameters.QUANTITY, quantity)
            }
            stdParams.put(Parameters.REFERRAL_FROM, referralFrom)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Add To Cart", mergedParams.toString())
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
            val stdParams = JSONObject()
            if (!value.isNaN()) {
                stdParams.put(Parameters.RATING_VALUE, value)
            }
            if (!maxRating.isNaN()) {
                stdParams.put(Parameters.MAX_RATING_VALUE, maxRating)
            }

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Rating", mergedParams.toString())
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
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, type)
            stdParams.put(Parameters.NAME, networkName)
            stdParams.put(Parameters.DURATION, placement)
            stdParams.put(Parameters.USER_ID, mediationName)
            stdParams.put(Parameters.NAME, campaignId)
            stdParams.put(Parameters.DURATION, campaignName)

            val mergedParams = mergeJSONObjects(stdParams, parameters)
            tracker.customEvent("Ad View", mergedParams.toString())
        } ?: run {
            tracker.adview(
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
        val userid = tutorial.optString(Parameters.USER_ID)
        val name = tutorial.optString(Parameters.NAME)
        val duration = tutorial.optDouble(Parameters.DURATION) as Double


        val parameters: JSONObject? = tutorial.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userid)
            stdParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                stdParams.put(Parameters.DURATION, duration)
            }

            val mergedParams = mergeJSONObjects(stdParams, parameters)

            tracker.customEvent("Tutorial Complete", mergedParams.toString())
        } ?: run {
            tracker.tutorialLevelComplete("Tutorial Complete", userid, name, duration)
        }
    }

    fun levelComplete(level: JSONObject) {
        val userid = level.optString(Parameters.USER_ID)
        val name = level.optString(Parameters.NAME)
        val duration = level.optDouble(Parameters.DURATION) as Double

        val parameters: JSONObject? = level.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userid)
            stdParams.put(Parameters.NAME, name)
            if (!duration.isNaN()) {
                stdParams.put(Parameters.DURATION, duration)
            }
            val mergedParams = mergeJSONObjects(stdParams, parameters)

            tracker.customEvent("Level Complete", mergedParams.toString())
        } ?: run {
            tracker.tutorialLevelComplete("Level Complete", userid, name, duration)
        }
    }

    fun purchase(purchase: JSONObject) {
        val userid = purchase.optString(Parameters.USER_ID)
        val name = purchase.optString(Parameters.NAME)
        val contentid = purchase.optString(Parameters.CONTENT_ID)
        val price = purchase.optDouble(Parameters.PRICE) as Double
        val currency = purchase.optString(Parameters.CURRENCY)
        val guestCheckout = purchase.optString(Parameters.CHECKOUT_AS_GUEST)
        val parameters: JSONObject? = purchase.optJSONObject(Parameters.CUSTOM_PARAMETERS)

        parameters?.let {
            // combine custom and standard params
            val stdParams = JSONObject()
            stdParams.put(Parameters.USER_ID, userid)
            stdParams.put(Parameters.NAME, name)
            stdParams.put(Parameters.CONTENT_ID, contentid)
            if (!price.isNaN()) {
                stdParams.put(Parameters.PRICE, price)
            }
            stdParams.put(Parameters.CURRENCY, currency)
            stdParams.put(Parameters.CHECKOUT_AS_GUEST, guestCheckout)
            val mergedParams = mergeJSONObjects(stdParams, parameters)

            tracker.customEvent("Purchase", mergedParams.toString())
        } ?: run {
            tracker.purchase(userid, name, contentid, price, currency, guestCheckout)
        }

    }

    fun mergeJSONObjects(json1: JSONObject, json2: JSONObject): JSONObject {
        val iter1 = json1.keys()
        val iter2 = json2.keys()
        val merged = JSONObject()
        while (iter1.hasNext()) {
            val key = iter1.next()
            if (json1.get(key) != null && json1.get(key) != "") {
                merged.put(key, json1.get(key))
            }
        }

        while (iter2.hasNext()) {
            val key = iter2.next()
            if (json2.get(key) != null && json2.get(key) != "") {
                merged.put(key, json2.get(key))
            }
        }
        return merged
    }

    fun createStandardParams(payload: JSONObject): JSONObject {
        val stdParams = JSONObject()

        val device_type = payload.optString(Parameters.DEVICE_TYPE)
        val content_type = payload.optString(Parameters.CONTENT_TYPE)
        val content_id = payload.optString(Parameters.CONTENT_ID)
        val checkout_as_guest = payload.optString(Parameters.CHECKOUT_AS_GUEST)
        val ad_mediation_name = payload.optString(Parameters.AD_MEDIATION_NAME)
        val ad_network_name = payload.optString(Parameters.AD_NETWORK_NAME)
        val ad_group_id = payload.optString(Parameters.AD_GROUP_ID)
        val ad_group_name = payload.optString(Parameters.AD_GROUP_NAME)
        val ad_size = payload.optString(Parameters.AD_SIZE)
        val ad_campaign_name = payload.optString(Parameters.AD_CAMPAIGN_NAME)
        val ad_campaign_id = payload.optString(Parameters.AD_CAMPAIGN_ID)
        val ad_type = payload.optString(Parameters.AD_TYPE)
        val placement = payload.optString(Parameters.PLACEMENT)
        val end_date = payload.optString(Parameters.END_DATE)
        val duration = payload.optDouble(Parameters.DURATION)
        val destination = payload.optString(Parameters.DESTINATION)
        val description = payload.optString(Parameters.DESCRIPTION)
        val date = payload.optString(Parameters.DATE)
        val currency = payload.optString(Parameters.CURRENCY)
        val origin = payload.optString(Parameters.ORIGIN)
        val order_id = payload.optString(Parameters.ORDER_ID)
        val name = payload.optString(Parameters.NAME)
        val max_rating_value = payload.optDouble(Parameters.MAX_RATING_VALUE)
        val level = payload.optString(Parameters.LEVEL)
        val item_added_from = payload.optString(Parameters.ITEM_ADDED_FROM)
        val source = payload.optString(Parameters.SOURCE)
        val uri = payload.optString(Parameters.URI)
        val completed = payload.optBoolean(Parameters.COMPLETED)
        val action = payload.optString(Parameters.ACTION)
        val background = payload.optBoolean(Parameters.BACKGROUND)
        val spatial_x = payload.optDouble(Parameters.SPATIAL_X)
        val spatial_y = payload.optDouble(Parameters.SPATIAL_Y)
        val spatial_z = payload.optDouble(Parameters.SPATIAL_Z)
        val validated = payload.optString(Parameters.VALIDATED)
        val username = payload.optString(Parameters.USER_NAME)
        val userid = payload.optString(Parameters.USER_ID)
        val success = payload.optString(Parameters.SUCCESS)
        val start_date = payload.optString(Parameters.START_DATE)
        val search_term = payload.optString(Parameters.SEARCH_TERM)
        val score = payload.optString(Parameters.SCORE)
        val results = payload.optString(Parameters.RESULTS)
        val registration_method = payload.optString(Parameters.REGISTRATION_METHOD)
        val referral_from = payload.optString(Parameters.REFERRAL_FROM)
        val receipt_id = payload.optString(Parameters.RECEIPT_ID)
        val rating_value = payload.optDouble(Parameters.RATING_VALUE)
        val quantity = payload.optDouble(Parameters.QUANTITY)
        val price = payload.optDouble(Parameters.PRICE)

        if (ad_type != "") {
            stdParams.put(Parameters.AD_TYPE, ad_type)
        }
        if (ad_campaign_id != "") {
            stdParams.put(Parameters.AD_CAMPAIGN_ID, ad_campaign_id)
        }
        if (ad_campaign_name != "") {
            stdParams.put(Parameters.AD_CAMPAIGN_NAME, ad_campaign_name)
        }
        if (ad_size != "") {
            stdParams.put(Parameters.AD_SIZE, ad_size)
        }
        if (ad_group_name != "") {
            stdParams.put(Parameters.AD_GROUP_NAME, ad_group_name)
        }
        if (ad_group_id != "") {
            stdParams.put(Parameters.AD_GROUP_ID, ad_group_id)
        }
        if (ad_network_name != "") {
            stdParams.put(Parameters.AD_NETWORK_NAME, ad_network_name)
        }
        if (ad_mediation_name != "") {
            stdParams.put(Parameters.AD_MEDIATION_NAME, ad_mediation_name)
        }
        if (content_id != "") {
            stdParams.put(Parameters.CONTENT_ID, content_id)
        }
        if (content_type != "") {
            stdParams.put(Parameters.CONTENT_TYPE, content_type)
        }
        if (device_type != "") {
            stdParams.put(Parameters.DEVICE_TYPE, device_type)
        }
        if (date != "") {
            stdParams.put(Parameters.DATE, date)
        }
        if (description != "") {
            stdParams.put(Parameters.DESCRIPTION, description)
        }
        if (destination != "") {
            stdParams.put(Parameters.DESTINATION, destination)
        }
        if (!duration.isNaN()) {
            stdParams.put(Parameters.DURATION, duration)
        }
        if (end_date != "") {
            stdParams.put(Parameters.END_DATE, end_date)
        }
        if (placement != "") {
            stdParams.put(Parameters.PLACEMENT, placement)
        }
        if (level != "") {
            stdParams.put(Parameters.LEVEL, level)
        }
        if (!max_rating_value.isNaN()) {
            stdParams.put(Parameters.MAX_RATING_VALUE, max_rating_value)
        }
        if (order_id != "") {
            stdParams.put(Parameters.ORDER_ID, order_id)
        }
        if (origin != "") {
            stdParams.put(Parameters.ORIGIN, origin)
        }
        if (currency != "") {
            stdParams.put(Parameters.CURRENCY, currency)
        }
        if (results != "") {
            stdParams.put(Parameters.RESULTS, results)
        }
        if (score != "") {
            stdParams.put(Parameters.SCORE, score)
        }
        if (search_term != "") {
            stdParams.put(Parameters.SEARCH_TERM, search_term)
        }
        if (start_date != "") {
            stdParams.put(Parameters.START_DATE, start_date)
        }
        if (success != "") {
            stdParams.put(Parameters.SUCCESS, success)
        }
        if (username != "") {
            stdParams.put(Parameters.USER_NAME, username)
        }
        if (validated != "") {
            stdParams.put(Parameters.VALIDATED, validated)
        }
        if (!spatial_x.isNaN()) {
            stdParams.put(Parameters.SPATIAL_X, spatial_x)
        }
        if (!spatial_y.isNaN()) {
            stdParams.put(Parameters.SPATIAL_Y, spatial_y)
        }
        if (!spatial_z.isNaN()) {
            stdParams.put(Parameters.SPATIAL_Z, spatial_z)
        }
        if (background == true || background == false) {
            stdParams.put(Parameters.BACKGROUND, background)
        }
        if (action != "") {
            stdParams.put(Parameters.ACTION, action)
        }
        if (completed == true || completed == false) {
            stdParams.put(Parameters.COMPLETED, completed)
        }
        if (receipt_id != "") {
            stdParams.put(Parameters.RECEIPT_ID, receipt_id)
        }
        if (referral_from != "") {
            stdParams.put(Parameters.REFERRAL_FROM, referral_from)
        }
        if (registration_method != "") {
            stdParams.put(Parameters.REGISTRATION_METHOD, registration_method)
        }
        if (uri != "") {
            stdParams.put(Parameters.URI, uri)
        }
        if (source != "") {
            stdParams.put(Parameters.SOURCE, source)
        }
        if (item_added_from != "") {
            stdParams.put(Parameters.ITEM_ADDED_FROM, item_added_from)
        }
        if (userid != "") {
            stdParams.put(Parameters.USER_ID, userid)
        }
        if (name != "") {
            stdParams.put(Parameters.NAME, name)
        }
        if (!price.isNaN()) {
            stdParams.put(Parameters.PRICE, price)
        }
        if (!quantity.isNaN()) {
            stdParams.put(Parameters.QUANTITY, quantity)
        }
        if (!rating_value.isNaN()) {
            stdParams.put(Parameters.RATING_VALUE, rating_value)
        }
        if (checkout_as_guest != "") {
            stdParams.put(Parameters.CHECKOUT_AS_GUEST, checkout_as_guest)
        }

        return stdParams
    }


}