package com.tealium.kochava

import Commands
import android.annotation.SuppressLint
import android.app.Application
import com.kochava.base.Tracker
import org.json.JSONObject

class KochavaTracker(application: Application, applicationId: String) : KochavaTrackable {

    init {
        Tracker.configure(Tracker.Configuration(application.applicationContext)
            .setAppGuid(applicationId)
        )
    }

    override fun configure(application: Application, configurationParams: JSONObject) {
        Tracker.configure(Tracker.Configuration(application.applicationContext)
            .addCustom(configurationParams)
        )
    }

    override fun setSleep(sleep: Boolean) {
        Tracker.setSleep(sleep)
    }

    @SuppressLint("CheckResult")
    override fun logEvent(eventName: String, payload: JSONObject) {
        var event: Tracker.Event
        if (eventName.equals(Commands.ACHIEVEMENT)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ACHIEVEMENT)
        } else if (eventName.equals(Commands.AD_VIEW)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_AD_VIEW)
        } else if (eventName.equals(Commands.ADD_TO_CART)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_CART)
        } else if (eventName.equals(Commands.ADD_TO_WISH_LIST)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_WISH_LIST)
        } else if (eventName.equals(Commands.CHECKOUT_START)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_CHECKOUT_START)
        } else if (eventName.equals(Commands.DEEP_LINK)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_DEEP_LINK)
        } else if (eventName.equals(Commands.LEVEL_COMPLETE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_LEVEL_COMPLETE)
        } else if (eventName.equals(Commands.PURCHASE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_PURCHASE)
        } else if (eventName.equals(Commands.RATING)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_RATING)
        } else if (eventName.equals(Commands.SEARCH)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_SEARCH)
        } else if (eventName.equals(Commands.START_TRIAL)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_START_TRIAL)
        } else if (eventName.equals(Commands.SUBSCRIBE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_SUBSCRIBE)
        } else if (eventName.equals(Commands.TUTORIAL_COMPLETE)) {
            event = Tracker.Event(Tracker.EVENT_TYPE_TUTORIAL_COMPLETE)
        } else {
            event = Tracker.Event(Tracker.EVENT_TYPE_VIEW)
        }

        event.addCustom(payload)

        Tracker.sendEvent(event)
    }

    override fun customEvent(eventName: String, parameters: String) {
        Tracker.sendEvent(eventName, parameters)
    }
}