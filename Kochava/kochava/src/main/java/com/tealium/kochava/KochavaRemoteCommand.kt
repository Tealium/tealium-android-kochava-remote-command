package com.tealium.kochava

import android.app.Application
import android.util.Log
import com.kochava.base.Tracker
import com.tealium.internal.tagbridge.RemoteCommand
import com.tealium.kochava.*
import org.json.JSONObject

open class KochavaRemoteCommand : RemoteCommand {

    private val TAG = this::class.java.simpleName

    lateinit var tracker: KochavaTrackable

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

    @JvmOverloads
    constructor(
        application: Application,
        appGuid: String,
        commandId: String = DEFAULT_COMMAND_ID,
        description: String = DEFAULT_COMMAND_DESCRIPTION
    ): super(commandId, description) {
//        Tracker.configure(Tracker.Configuration(application.applicationContext).setAppGuid(appGuid))
        tracker.initialize(application, appGuid)
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
            println("commands " + command)
            when (command) {
                Commands.PURCHASE -> {
                    val purchase: JSONObject? = payload.optJSONObject(Purchase.PURCHASE)
                    purchase?.let {
                        purchase(it)
                    } ?: run {
                        Log.e(TAG, "${Purchase.PURCHASE} $REQUIRED_KEY")
                    }
                }
                Commands.TUTORIAL_COMPLETE -> {
                    val tutorialComplete: JSONObject? = payload.optJSONObject(TutorialComplete.TUTORIAL_COMPLETE)
                    tutorialComplete?.let {
                        tutorialComplete(it)
                    } ?: run {
                        Log.e(TAG, "${TutorialComplete.TUTORIAL_COMPLETE} $REQUIRED_KEY")
                    }
                }
                Commands.LEVEL_COMPLETE -> {
                    val levelComplete: JSONObject? = payload.optJSONObject(LevelComplete.LEVEL_COMPLETE)
                    levelComplete?.let {
                        levelComplete(it)
                    } ?: run {
                        Log.e(TAG, "${LevelComplete.LEVEL_COMPLETE} $REQUIRED_KEY")
                    }
                }
                Commands.AD_VIEW -> {
                    val adview: JSONObject? = payload.optJSONObject(AdView.AD_VIEW)
                    adview?.let {
                        adView(it)
                    } ?: run {
                        Log.e(TAG, "${AdView.AD_VIEW} $REQUIRED_KEY")
                    }
                }
                Commands.RATING -> {
                    val rating: JSONObject? = payload.optJSONObject(Rating.RATING)
                    rating?.let {
                        rating(it)
                    } ?: run {
                        Log.e(TAG, "${Rating.RATING} $REQUIRED_KEY")
                    }
                }
                Commands.ADD_TO_CART -> {
                    val cart: JSONObject? = payload.optJSONObject(AddToCart.ADD_TO_CART)
                    cart?.let {
                        addToCart(it)
                    } ?: run {
                        Log.e(TAG, "${AddToCart.ADD_TO_CART} $REQUIRED_KEY")
                    }
                }
                Commands.ADD_TO_WISH_LIST -> {
                    val wishlist: JSONObject? = payload.optJSONObject(AddToWishList.ADD_TO_WISH_LIST)
                    wishlist?.let {
                        addToWishList(it)
                    } ?: run {
                        Log.e(TAG, "${AddToWishList.ADD_TO_WISH_LIST} $REQUIRED_KEY")
                    }
                }
                Commands.CHECKOUT_START -> {
                    val checkout: JSONObject? = payload.optJSONObject(CheckoutStart.CHECKOUT_START)
                    checkout?.let {
                        checkoutStart(it)
                    } ?: run {
                        Log.e(TAG, "${CheckoutStart.CHECKOUT_START} $REQUIRED_KEY")
                    }
                }
                Commands.SEARCH -> {
                    val search: JSONObject? = payload.optJSONObject(Search.SEARCH)
                    search?.let {
                        search(it)
                    } ?: run {
                        Log.e(TAG, "${Search.SEARCH} $REQUIRED_KEY")
                    }
                }
                Commands.REGISTRATION_COMPLETE -> {
                    val registration: JSONObject? = payload.optJSONObject(RegistrationComplete.REGISTRATION_COMPLETE)
                    registration?.let {
                        register(it)
                    } ?: run {
                        Log.e(TAG, "${RegistrationComplete.REGISTRATION_COMPLETE} $REQUIRED_KEY")
                    }
                }
                Commands.VIEW -> {
                    val view: JSONObject? = payload.optJSONObject(View.VIEW)
                    view?.let {
                        view(it)
                    } ?: run {
                        Log.e(TAG, "${View.VIEW} $REQUIRED_KEY")
                    }
                }
                Commands.ACHIEVEMENT -> {
                    val achievement: JSONObject? = payload.optJSONObject(View.VIEW)
                    achievement?.let {
                        achieve(it)
                    } ?: run {
                        Log.e(TAG, "${Achievement.ACHIEVEMENT} $REQUIRED_KEY")
                    }
                }
            }
        }
    }

    fun achieve(achievement: JSONObject) {
        val userid = achievement.optString(Achievement.ACHIEVEMENT_USER_ID)
        val name = achievement.optString(Achievement.ACHIEVEMENT_NAME)
        val duration = achievement.optDouble(Achievement.ACHIEVEMENT_DURATION) as Double
        duration?.let {
            if (it.isNaN()) {
                return
            }
        }
        val parameters: JSONObject? = achievement.optJSONObject(Achievement.ACHIEVEMENT_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Achievement", parameters)
        } ?: run {
            tracker.achievement(userid, name, duration)
        }
    }

    fun view(view: JSONObject) {
        val userId = view.optString(View.VIEW_USER_ID)
        val name = view.optString(View.VIEW_NAME)
        val contentId = view.optString(View.VIEW_CONTENT_ID)
        val referralForm = view.optString(View.VIEW_REFERRAL_FORM)

        val parameters: JSONObject? = view.optJSONObject(view.optString(View.VIEW_PARAMETERS))

        parameters?.let { purchaseParameters ->
            tracker.customEvent("View", parameters)
        } ?: run {
            tracker.view(userId, name, contentId, referralForm)
        }
    }

    fun register(registration: JSONObject) {
        val userId = registration.optString(RegistrationComplete.REGISTRATION_COMPLETE_USER_ID)
        val userName = registration.optString(RegistrationComplete.REGISTRATION_COMPLETE_USER_NAME)
        val referralForm = registration.optString(RegistrationComplete.REGISTRATION_COMPLETE_REFERRALFORM)

        val parameters: JSONObject? = registration.optJSONObject(RegistrationComplete.REGISTRATION_COMPLETE_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Registration Complete", parameters)
        } ?: run {
            tracker.registrationComplete(userId, userName, referralForm)
        }
    }

    fun search(search: JSONObject) {
        val uri = search.optString(Search.SEARCH_URI)
        val results = search.optString(Search.SEARCH_RESULTS)

        val parameters: JSONObject? = search.optJSONObject(Search.SEARCH_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Search", parameters)
        } ?: run {
            tracker.search(uri, results)
        }
    }

    fun checkoutStart(checkoutStart: JSONObject) {
        val userId = checkoutStart.optString(CheckoutStart.CHECKOUT_START_USER_ID)
        val name = checkoutStart.optString(CheckoutStart.CHECKOUT_START_NAME)
        val contentId = checkoutStart.optString(CheckoutStart.CHECKOUT_START_CONTENT_ID)
        val guestCheckout = checkoutStart.optString(CheckoutStart.CHECKOUT_START_CHECKOUT_AS_GUEST)
        val currency = checkoutStart.optString(CheckoutStart.CHECKOUT_START_CURRENCY)

        val parameters: JSONObject? = checkoutStart.optJSONObject(CheckoutStart.CHECKOUT_START_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Checkout Start", parameters)
        } ?: run {
            tracker.checkoutStart(userId, name, contentId, guestCheckout, currency)
        }
    }

    fun addToWishList(wishlist: JSONObject) {
        val userId = wishlist.optString(AddToWishList.ADDTOWISHLIST_USER_ID)
        val name = wishlist.optString(AddToWishList.ADDTOWISHLIST_NAME)
        val contentId = wishlist.optString(AddToWishList.ADDTOWISHLIST_CONTENT_ID)
        val referralForm = wishlist.optString(AddToWishList.ADDTOWISHLIST_REFERRALFORM)

        val parameters: JSONObject? = wishlist.optJSONObject(AddToWishList.ADDTOWISHLIST_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Add To Wishlist", parameters)
        } ?: run {
            tracker.addToWishList(userId, name, contentId, referralForm)
        }
    }

    fun addToCart(cart: JSONObject) {
        val userId = cart.optString(AddToCart.ADDTOCART_USER_ID)
        val name = cart.optString(AddToCart.ADDTOCART_NAME)
        val contentId = cart.optString(AddToCart.ADDTOCART_CONTENT_ID)
        val quantity = cart.optDouble(AddToCart.ADDTOCART_QUANTITY)
        val referralForm = cart.optString(AddToCart.ADDTOCART_REFERRALFORM)

        val parameters: JSONObject? = cart.optJSONObject(AddToCart.ADDTOCART_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Add To Cart", parameters)
        } ?: run {
            tracker.addToCart(userId, name, contentId, quantity, referralForm)
        }
    }

    fun rating(rating: JSONObject) {
        val value = rating.optDouble(Rating.RATING_VALUE)
        value?.let {
            if (it.isNaN()) {
                return
            }
        }
        val maxRating = rating.optDouble(Rating.RATING_MAXIMUM_RATING)
        maxRating?.let {
            if (it.isNaN()) {
                return
            }
        }
        val parameters: JSONObject? = rating.optJSONObject(Rating.RATING_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Rating", parameters)
        } ?: run {
            tracker.rating(value, maxRating)
        }
    }

    fun adView(adview: JSONObject) {
        val type = adview.optString(AdView.ADVIEW_TYPE)
        val networkName = adview.optString(AdView.ADVIEW_NETWORK_NAME)
        val placement = adview.optString(AdView.ADVIEW_PLACEMENT)
        val mediationName = adview.optString(AdView.ADVIEW_MEDIATION_NAME)
        val campaignId = adview.optString(AdView.ADVIEW_CAMPAIGN_ID)
        val campaignName = adview.optString(AdView.ADVIEW_CAMPAIGN_NAME)
        val size = adview.optString(AdView.ADVIEW_SIZE)

        val parameters: JSONObject? = adview.optJSONObject(AdView.ADVIEW_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Ad View", parameters)
        } ?: run {
            tracker.adview(type, networkName, placement, mediationName, campaignId, campaignName, size)
        }
    }

    fun tutorialComplete(tutorial: JSONObject) {
        val userid = tutorial.optString(TutorialComplete.TUTORIAL_USER_ID)
        val name = tutorial.optString(TutorialComplete.TUTORIAL_NAME)
        val duration = tutorial.optDouble(TutorialComplete.TUTORIAL_DURATION) as Double
        duration?.let {
            if (it.isNaN()) {
                return
            }
        }
        val parameters: JSONObject? = tutorial.optJSONObject(TutorialComplete.TUTORIAL_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Tutorial Complete", parameters)
        } ?: run {
            tracker.tutorialLevelComplete("Tutorial Complete", userid, name, duration)
        }
    }

    fun levelComplete(level: JSONObject) {
        val userid = level.optString(LevelComplete.LEVEL_USER_ID)
        val name = level.optString(LevelComplete.LEVEL_NAME)
        val duration = level.optDouble(LevelComplete.LEVEL_DURATION) as Double
        duration?.let {
            if (it.isNaN()) {
                return
            }
        }
        val parameters: JSONObject? = level.optJSONObject(TutorialComplete.TUTORIAL_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Level Complete", parameters)
        } ?: run {
            tracker.tutorialLevelComplete("Level Complete", userid, name, duration)
        }
    }

    fun purchase(purchase: JSONObject) {
        println("Purchase called")
        val userid = purchase.optString(Purchase.PURCHASE_USER_ID)
        val name = purchase.optString(Purchase.PURCHASE_NAME)
        val contentid = purchase.optString(Purchase.PURCHASE_CONTENT_ID)
        val price = purchase.optDouble(Purchase.PURCHASE_PRICE) as Double
        price?.let {
            if (it.isNaN()) {
                return
            }
        }
        val currency = purchase.optString(Purchase.PURCHASE_CURRENCY, "USD")
        val guestCheckout = purchase.optString(Purchase.PURCHASE_CHECKOUT_AS_GUEST)
        val parameters: JSONObject? = purchase.optJSONObject(Purchase.PURCHASE_PARAMETERS)

        parameters?.let {
            tracker.customEvent("Purchase", parameters)
        } ?: run {
            tracker.purchase(userid, name, contentid, price, currency, guestCheckout)
        }

    }



}