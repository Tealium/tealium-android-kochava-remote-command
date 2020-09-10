package com.tealium.kochava

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

    override fun standardEvent(
        eventName: String,
        parameters: String
    ) {
        if (eventName.equals(Commands.ACHIEVEMENT)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_ACHIEVEMENT).toString(), parameters)
        } else if (eventName.equals(Commands.AD_VIEW)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_AD_VIEW).toString(), parameters)
        } else if (eventName.equals(Commands.ADD_TO_CART)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_CART).toString(), parameters)
        } else if (eventName.equals(Commands.ADD_TO_WISH_LIST)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_ADD_TO_WISH_LIST).toString(), parameters)
        } else if (eventName.equals(Commands.CHECKOUT_START)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_CHECKOUT_START).toString(), parameters)
        } else if (eventName.equals(Commands.DEEP_LINK)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_DEEP_LINK).toString(), parameters)
        } else if (eventName.equals(Commands.LEVEL_COMPLETE)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_LEVEL_COMPLETE).toString(), parameters)
        } else if (eventName.equals(Commands.PURCHASE)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_PURCHASE).toString(), parameters)
        } else if (eventName.equals(Commands.RATING)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_RATING).toString(), parameters)
        } else if (eventName.equals(Commands.SEARCH)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_SEARCH).toString(), parameters)
        } else if (eventName.equals(Commands.START_TRIAL)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_START_TRIAL).toString(), parameters)
        } else if (eventName.equals(Commands.SUBSCRIBE)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_SUBSCRIBE).toString(), parameters)
        } else if (eventName.equals(Commands.TUTORIAL_COMPLETE)) {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_TUTORIAL_COMPLETE).toString(), parameters)
        } else {
            Tracker.sendEvent(Tracker.Event(Tracker.EVENT_TYPE_VIEW).toString(), parameters)
        }
    }

    override fun customEvent(eventName: String, parameters: String) {
        Tracker.sendEvent(eventName, parameters)
    }
}