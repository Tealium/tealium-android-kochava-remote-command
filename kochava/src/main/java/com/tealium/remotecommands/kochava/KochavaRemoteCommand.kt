package com.tealium.remotecommands.kochava

import android.app.Application
import com.tealium.remotecommands.RemoteCommand
import org.json.JSONObject

class KochavaRemoteCommand @JvmOverloads constructor(
    application: Application,
    appGui: String,
    commandId: String = DEFAULT_COMMAND_ID,
    description: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, description) {

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
                    val configurationParams = payload.optJSONObject(EventKey.CONFIGURATION_PARAMS)
                    kochavaInstance.initialize(configurationParams)
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
                    val customParameters = payload.optJSONObject(EventKey.CUSTOM)
                    kochavaInstance.sendCustomEvent(customEventName, customParameters)
                }
                else -> {
                    standardEvent(command)?.let { kochavaEventType ->
                        val eventParameters = payload.optJSONObject(EventKey.EVENT_PARAMS)
                        val customParameters = payload.optJSONObject(EventKey.CUSTOM)
                        eventParameters?.put(EventKey.INFO_DICTIONARY, customParameters)
                        kochavaInstance.sendEvent(kochavaEventType, eventParameters)
                    }
                }
            }
        }
    }

    /**
     * Returns the Facebook standard event name if it exists.
     *
     * @param commandName - name of the Tealium command name.
     */
    fun standardEvent(commandName: String): Int? {
        return StandardEvents.names[commandName]
    }

    fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(EventKey.COMMAND_KEY)
        return command.split(EventKey.SEPARATOR.toRegex())
            .dropLastWhile { it.isEmpty() }
            .map { it.trim().toLowerCase() }
            .toTypedArray()
    }

    companion object {
        const val DEFAULT_COMMAND_ID = "kochava"
        const val DEFAULT_COMMAND_DESCRIPTION = "Tealium-Kochava Remote Command"
    }
}