package com.tealium.example

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.tealium.example.kochava.BuildConfig
import com.tealium.library.Tealium

class App: Application() {

    lateinit var tealium: Tealium

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val config = Tealium.Config.create(this, "tealiummobile", "kochava", "dev")
        config.forceOverrideLogLevel = "dev"
        tealium = Tealium.createInstance(TealiumHelper.instanceName, config)
    }
}