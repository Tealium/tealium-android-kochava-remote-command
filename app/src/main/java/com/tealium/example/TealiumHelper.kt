package com.tealium.example

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.tealium.kochava.KochavaRemoteCommand
import com.tealium.library.Tealium

object TealiumHelper {

    lateinit var tealium: Tealium
    val instanceName = "tealium_instance"
    private val appGuid = "your_dev_key"

    fun initialize(application: Application) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val config = Tealium.Config.create(application, "tealiummobile", "kochava", "dev")
        config.forceOverrideLogLevel = "dev"
        tealium = Tealium.createInstance(TealiumHelper.instanceName, config)
        val kochavaRemoteCommand = KochavaRemoteCommand(application, appGuid)
        tealium.addRemoteCommand(kochavaRemoteCommand)
    }

    fun trackView(viewName: String) {
        Tealium.getInstance(instanceName)?.trackView(viewName, null)
    }

    fun trackView(viewName: String, data: Map<String, Any>?) {
        Tealium.getInstance(instanceName)?.trackView(viewName, data)
    }

    fun trackEvent(eventName: String, data: Map<String, Any>?) {
        Tealium.getInstance(instanceName)?.trackEvent(eventName, data)
    }
}