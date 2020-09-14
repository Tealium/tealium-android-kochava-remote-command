package com.tealium.kochava

import android.app.Application
import org.json.JSONObject

interface KochavaTrackable {

    fun configure(application: Application, parameters: JSONObject)

    fun setSleep(sleep: Boolean)

    fun logEvent(eventName: String, payload: JSONObject)

    fun customEvent(eventName: String, parameters: String)
}