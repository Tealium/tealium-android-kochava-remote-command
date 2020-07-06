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
        tracker.customEvent(eventName, payload.toString())
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

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }

}