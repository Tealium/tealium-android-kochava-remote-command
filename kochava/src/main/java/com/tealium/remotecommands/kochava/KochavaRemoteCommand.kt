package com.tealium.remotecommands.kochava

import android.app.Application
import com.tealium.remotecommands.RemoteCommand
import org.json.JSONObject
import java.util.*

class KochavaRemoteCommand @JvmOverloads constructor(
    application: Application,
    appGui: String? = null,
    commandId: String = DEFAULT_COMMAND_ID,
    description: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, description, BuildConfig.TEALIUM_KOCHAVA_VERSION) {

    var kochavaInstance: KochavaCommand = KochavaInstance(application, appGui)

    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            when (command) {
                Commands.INITIALIZE -> {
                    kochavaInstance.initialize(payload)
                }
                Commands.SET_IDENTITY_LINK -> {
                    val id = payload.optJSONObject(EventKey.IDENTITY_LINKS)
                    id?.let {
                        kochavaInstance.setIdentityLink(id)
                    }
                }
                Commands.SLEEP_TRACKER -> {
                    val sleep = payload.optBoolean(EventKey.SLEEP)
                    kochavaInstance.setSleep(sleep)
                }
                Commands.CUSTOM_EVENT -> {
                    val customEventName = payload.optString(EventKey.CUSTOM_EVENT_NAME)
                    // TIQ Remote Command Tag
                    if (payload.optJSONObject(EventKey.EVENT_PARAMS) != null) {
                        val eventParameters = payload.optJSONObject(EventKey.EVENT_PARAMS)
                        kochavaInstance.sendCustomEvent(customEventName, eventParameters)
                        return
                    }
                    // JSON Remote Command
                    else {
                        val customParameters = payload.optJSONObject(EventKey.CUSTOM)
                        kochavaInstance.sendCustomEvent(customEventName, customParameters)
                        return
                    }
                }
                else -> {
                    standardEvent(command)?.let { kochavaEventType ->
                        // TIQ Remote Command Tag
                        if (payload.optJSONObject(EventKey.EVENT_PARAMS) != null) {
                            val eventParameters = payload.optJSONObject(EventKey.EVENT_PARAMS)
                            val eventCustomParameters =
                                eventParameters?.optJSONObject(EventKey.INFO_DICTIONARY)
                            kochavaInstance.sendEvent(
                                kochavaEventType,
                                eventParameters,
                                eventCustomParameters
                            )
                            return
                            // JSON Remote Command
                        } else {
                            val eventParameters = payload.optJSONObject(EventKey.EVENT)
                            val eventCustomParameters = payload.optJSONObject(EventKey.CUSTOM)
                            kochavaInstance.sendEvent(
                                kochavaEventType,
                                eventParameters,
                                eventCustomParameters
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the Kochava standard event name if it exists.
     *
     * @param commandName - name of the Tealium command name.
     */
    fun standardEvent(commandName: String): Int? {
        return StandardEvents.names[commandName]
    }

    fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(EventKey.COMMAND_KEY)
        return command.split(EventKey.SEPARATOR.toRegex())
            .map { it.trim().toLowerCase(Locale.ROOT) }
            .toTypedArray()
    }

    companion object {
        const val DEFAULT_COMMAND_ID = "kochava"
        const val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }
}