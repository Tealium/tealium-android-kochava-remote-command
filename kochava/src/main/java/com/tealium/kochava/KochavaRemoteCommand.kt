package com.tealium.kochava

import Commands
import EventParameters
import Parameters
import android.app.Application
import com.tealium.internal.tagbridge.RemoteCommand
import org.json.JSONObject

class KochavaRemoteCommand : RemoteCommand {

    lateinit var tracker: KochavaTrackable
    var application: Application? = null

    /**
     * Constructs a RemoteCommand that integrates with the Kochava App Events SDK to allow Kochava API calls to be implemented through Tealium.
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
                    logEvent(Commands.PURCHASE, eventParams)
                }
                Commands.TUTORIAL_COMPLETE -> {
                    logEvent(Commands.TUTORIAL_COMPLETE, eventParams)
                }
                Commands.LEVEL_COMPLETE -> {
                    logEvent(Commands.LEVEL_COMPLETE, eventParams)
                }
                Commands.AD_VIEW -> {
                    logEvent(Commands.AD_VIEW, eventParams)
                }
                Commands.RATING -> {
                    logEvent(Commands.RATING, eventParams)
                }
                Commands.ADD_TO_CART -> {
                    logEvent(Commands.ADD_TO_CART, eventParams)
                }
                Commands.ADD_TO_WISH_LIST -> {
                    logEvent(Commands.ADD_TO_WISH_LIST, eventParams)
                }
                Commands.CHECKOUT_START -> {
                    logEvent(Commands.CHECKOUT_START, eventParams)
                }
                Commands.SEARCH -> {
                    logEvent(Commands.SEARCH, eventParams)
                }
                Commands.REGISTRATION_COMPLETE -> {
                    logEvent(Commands.REGISTRATION_COMPLETE, eventParams)
                }
                Commands.VIEW -> {
                    logEvent(Commands.VIEW, eventParams)
                }
                Commands.ACHIEVEMENT -> {
                    logEvent(Commands.ACHIEVEMENT, eventParams)
                }
                Commands.DEEP_LINK -> {
                    logEvent(Commands.DEEP_LINK, eventParams)
                }
                Commands.SUBSCRIBE -> {
                    logEvent(Commands.SUBSCRIBE, eventParams)
                }
                Commands.START_TRIAL -> {
                    logEvent(Commands.START_TRIAL, eventParams)
                }
                Commands.SET_SLEEP -> {
                    setSleep(eventParams)
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

    private fun logEvent(event: String, payload: JSONObject) {
        val customParameters: JSONObject? = payload.optJSONObject(EventParameters.CUSTOM_PARAMETERS)

        customParameters?.let {
            tracker.customEvent(event, payload.toString())
        } ?: run {
            getParameters(event, payload)
        }
    }

    private fun logCustomEvent(eventName: String, payload: JSONObject) {
        tracker.customEvent(eventName, payload.toString())
    }

    private fun setSleep(setsleep: JSONObject) {
        val sleep = setsleep.optBoolean(EventParameters.SLEEP)
        tracker.setSleep(sleep)
    }

    private fun getParameters(event: String, parameters: JSONObject) {
        val deviceType = parameters.optString(EventParameters.DEVICE_TYPE)
        val placement = parameters.optString(EventParameters.PLACEMENT)
        val adType = parameters.optString(EventParameters.AD_TYPE)
        val adCampaignId = parameters.optString(EventParameters.AD_CAMPAIGN_ID)
        val adCampaignName = parameters.optString(EventParameters.AD_CAMPAIGN_ID)
        val adSize = parameters.optString(EventParameters.AD_SIZE)
        val adGroupName = parameters.optString(EventParameters.AD_GROUP_NAME)
        val adGroupId = parameters.optString(EventParameters.AD_GROUP_ID)
        val adNetworkName = parameters.optString(EventParameters.AD_NETWORK_NAME)
        val adMediationName = parameters.optString(EventParameters.AD_MEDIATION_NAME)
        val checkoutAsGuest = parameters.optString(EventParameters.CHECKOUT_AS_GUEST)
        val contentId = parameters.optString(EventParameters.CONTENT_ID)
        val contentType = parameters.optString(EventParameters.CONTENT_TYPE)
        val currency = parameters.optString(EventParameters.CURRENCY)
        val date = parameters.optString(EventParameters.DATE)
        val description = parameters.optString(EventParameters.DESCRIPTION)
        val destination = parameters.optString(EventParameters.DESTINATION)
        val duration = parameters.optDouble(EventParameters.DURATION)
        val source = parameters.optString(EventParameters.SOURCE)
        val uri = parameters.optString(EventParameters.URI)
        val completed = parameters.optBoolean(EventParameters.COMPLETED)
        val action = parameters.optString(EventParameters.ACTION)
        val background = parameters.optBoolean(EventParameters.BACKGROUND)
        val spatialX = parameters.optDouble(EventParameters.SPATIAL_X)
        val spatialY = parameters.optDouble(EventParameters.SPATIAL_Y)
        val spatialZ = parameters.optDouble(EventParameters.SPATIAL_Z)
        val validated = parameters.optString(EventParameters.VALIDATED)
        val userName = parameters.optString(EventParameters.USER_NAME)
        val userID = parameters.optString(EventParameters.USER_ID)
        val success = parameters.optString(EventParameters.SUCCESS)
        val startDate = parameters.optString(EventParameters.START_DATE)
        val endDate = parameters.optString(EventParameters.END_DATE)
        val searchTerm = parameters.optString(EventParameters.SEARCH_TERM)
        val score = parameters.optString(EventParameters.SCORE)
        val results = parameters.optString(EventParameters.RESULTS)
        val registrationMethod = parameters.optString(EventParameters.REGISTRATION_METHOD)
        val referralFrom = parameters.optString(EventParameters.REFERRAL_FROM)
        val receiptId = parameters.optString(EventParameters.RECEIPT_ID)
        val ratingValue = parameters.optDouble(EventParameters.RATING_VALUE)
        val quantity = parameters.optDouble(EventParameters.QUANTITY)
        val price = parameters.optDouble(EventParameters.PRICE)
        val origin = parameters.optString(EventParameters.ORIGIN)
        val orderId = parameters.optString(EventParameters.ORDER_ID)
        val name = parameters.optString(EventParameters.NAME)
        val maxRatingValue = parameters.optDouble(EventParameters.MAX_RATING_VALUE)
        val level = parameters.optString(EventParameters.LEVEL)
        val itemAddedFrom = parameters.optString(EventParameters.ITEM_ADDED_FROM)

        tracker.logEvent(
            event, deviceType, placement, adType, adCampaignId, adCampaignName, adSize, adGroupName, adGroupId, adNetworkName, adMediationName,
            checkoutAsGuest, contentId, contentType, currency, date, description, destination, duration, source, uri, completed, action, background,
            spatialX, spatialY, spatialZ, validated, userName, userID, success, startDate, endDate, searchTerm, source, score, results, registrationMethod,
            referralFrom, receiptId, ratingValue, quantity, price, origin, orderId, name, maxRatingValue, level, itemAddedFrom
        )
    }

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }

}