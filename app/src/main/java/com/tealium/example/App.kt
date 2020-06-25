package com.tealium.example

import android.app.Application
import com.tealium.library.Tealium

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        TealiumHelper.initialize(this)
    }
}