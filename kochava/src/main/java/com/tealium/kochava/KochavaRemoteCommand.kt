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
                    logStandardEvent(Commands.PURCHASE, eventParams)
                }
                Commands.TUTORIAL_COMPLETE -> {
                    logStandardEvent(Commands.TUTORIAL_COMPLETE, eventParams)
                }
                Commands.LEVEL_COMPLETE -> {
                    logStandardEvent(Commands.LEVEL_COMPLETE, eventParams)
                }
                Commands.AD_VIEW -> {
                    logStandardEvent(Commands.AD_VIEW, eventParams)
                }
                Commands.RATING -> {
                    logStandardEvent(Commands.RATING, eventParams)
                }
                Commands.ADD_TO_CART -> {
                    logStandardEvent(Commands.ADD_TO_CART, eventParams)
                }
                Commands.ADD_TO_WISH_LIST -> {
                    logStandardEvent(Commands.ADD_TO_WISH_LIST, eventParams)
                }
                Commands.CHECKOUT_START -> {
                    logStandardEvent(Commands.CHECKOUT_START, eventParams)
                }
                Commands.SEARCH -> {
                    logStandardEvent(Commands.SEARCH, eventParams)
                }
                Commands.REGISTRATION_COMPLETE -> {
                    logStandardEvent(Commands.REGISTRATION_COMPLETE, eventParams)
                }
                Commands.VIEW -> {
                    logStandardEvent(Commands.VIEW, eventParams)
                }
                Commands.ACHIEVEMENT -> {
                    logStandardEvent(Commands.ACHIEVEMENT, eventParams)
                }
                Commands.DEEP_LINK -> {
                    logStandardEvent(Commands.DEEP_LINK, eventParams)
                }
                Commands.SUBSCRIBE -> {
                    logStandardEvent(Commands.SUBSCRIBE, eventParams)
                }
                Commands.START_TRIAL -> {
                    logStandardEvent(Commands.START_TRIAL, eventParams)
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

    private fun logStandardEvent(event: String, parameters: JSONObject) {
        tracker.standardEvent(event, parameters.toString())
    }

    private fun logCustomEvent(eventName: String, payload: JSONObject) {
        tracker.customEvent(eventName, payload.toString())
    }

    private fun setSleep(setsleep: JSONObject) {
        val sleep = setsleep.optBoolean(EventParameters.SLEEP)
        tracker.setSleep(sleep)
    }

    companion object {
        val DEFAULT_COMMAND_ID = "kochava"
        val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }

}