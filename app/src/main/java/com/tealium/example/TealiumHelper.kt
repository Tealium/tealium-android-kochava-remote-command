package com.tealium.example

import android.app.Application
import android.webkit.WebView
import com.tealium.core.*
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.lifecycle.Lifecycle
import com.tealium.remotecommanddispatcher.RemoteCommands
import com.tealium.remotecommanddispatcher.remoteCommands
import com.tealium.remotecommands.kochava.KochavaRemoteCommand
import com.tealium.tagmanagementdispatcher.TagManagement

object TealiumHelper {

    // Identifier for the main Tealium instance
    val TEALIUM_MAIN = "main"
    private lateinit var tealium: Tealium
    private val appGuid = "your_dev_key"

    fun initialize(application: Application) {

        WebView.setWebContentsDebuggingEnabled(true)


        val config = TealiumConfig(
            application,
            "tealiummobile",
            "kochava",
            Environment.DEV,
            dispatchers = mutableSetOf(
                Dispatchers.TagManagement,
                Dispatchers.RemoteCommands
            ),
            modules = mutableSetOf(Modules.Lifecycle)
        ).apply {
            useRemoteLibrarySettings = true
        }

        tealium = Tealium.create(TEALIUM_MAIN, config) {
            val kochavaRemoteCommand = KochavaRemoteCommand(application, appGuid)

            // Remote Command Tag - requires TiQ
//            remoteCommands?.add(kochavaRemoteCommand)

            // JSON Remote Command - requires local filename or url to remote file
            remoteCommands?.add(kochavaRemoteCommand, remoteUrl = "kochava.json")
        }
    }

    fun trackView(viewName: String, data: Map<String, Any>?) {
        tealium.track(TealiumView(viewName, data))
    }

    fun trackEvent(eventName: String, data: Map<String, Any>?) {
        tealium.track(TealiumEvent(eventName, data))
    }
}