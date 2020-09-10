package com.tealium.kochava

import android.app.Application
import org.json.JSONObject

interface KochavaTrackable {

    fun configure(application: Application, parameters: JSONObject)

    fun setSleep(sleep: Boolean)

    fun standardEvent(eventName: String, parameters: String)

    fun customEvent(eventName: String, parameters: String)
}