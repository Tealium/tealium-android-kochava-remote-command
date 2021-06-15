package com.tealium.remotecommands.kochava

import org.json.JSONObject

interface KochavaCommand {
    fun initialize(parameters: JSONObject? = null)

    // Send standard event
    fun sendEvent(standardEventType: Int, parameters: JSONObject? = null)

    // Send custom event
    fun sendCustomEvent(eventName: String, parameters: JSONObject? = null)

    fun setSleep(sleep: Boolean)
    fun setIdentityLink(identity: JSONObject)
}