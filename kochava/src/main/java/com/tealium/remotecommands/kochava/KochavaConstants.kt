package com.tealium.remotecommands.kochava

import com.kochava.base.Tracker

object EventKey {
    const val SEPARATOR = ","

    const val COMMAND_KEY = "command_name"
    const val EVENT = "event"
    const val EVENT_PARAMS = "event_parameters"
    const val INFO_DICTIONARY = "info_dictionary"
    const val CUSTOM = "custom"
    const val CUSTOM_EVENT_NAME = "custom_event_name"

    const val APP_GUID = "app_guid"
    const val LOG_LEVEL = "log_level"
    const val IDENTITY_LINKS = "identity_link_ids"
    const val LIMIT_AD_TRACKING = "limit_ad_tracking"
    const val SLEEP = "sleep_tracker"
}

object Commands {
    const val INITIALIZE = "initialize"
    const val SET_IDENTITY_LINK = "setidentitylink"
    const val SLEEP_TRACKER = "sleeptracker"
    const val CUSTOM_EVENT = "custom"
}

object StandardEvents {
    val names = mapOf(
        "achievement" to Tracker.EVENT_TYPE_ACHIEVEMENT,
        "addtocart" to Tracker.EVENT_TYPE_ADD_TO_CART,
        "addtowishlist" to Tracker.EVENT_TYPE_ADD_TO_WISH_LIST,
        "checkoutstart" to Tracker.EVENT_TYPE_CHECKOUT_START,
        "levelcomplete" to Tracker.EVENT_TYPE_LEVEL_COMPLETE,
        "purchase" to Tracker.EVENT_TYPE_PURCHASE,
        "rating" to Tracker.EVENT_TYPE_RATING,
        "registrationcomplete" to Tracker.EVENT_TYPE_REGISTRATION_COMPLETE,
        "search" to Tracker.EVENT_TYPE_SEARCH,
        "tutorialcomplete" to Tracker.EVENT_TYPE_TUTORIAL_COMPLETE,
        "view" to Tracker.EVENT_TYPE_VIEW,
        "adview" to Tracker.EVENT_TYPE_AD_VIEW,
        "pushreceived" to Tracker.EVENT_TYPE_PUSH_RECEIVED,
        "pushopened" to Tracker.EVENT_TYPE_PUSH_OPENED,
        "consentgranted" to Tracker.EVENT_TYPE_CONSENT_GRANTED,
        "deeplink" to Tracker.EVENT_TYPE_DEEP_LINK,
        "adclick" to Tracker.EVENT_TYPE_AD_CLICK,
        "starttrial" to Tracker.EVENT_TYPE_START_TRIAL,
        "subscribe" to Tracker.EVENT_TYPE_SUBSCRIBE
    )
}

object ParameterKey {
    const val DEVICE_TYPE = "device_type"
    const val PLACEMENT = "placement"
    const val AD_TYPE = "ad_type"
    const val AD_CAMPAIGN_ID = "ad_campaign_id"
    const val AD_CAMPAIGN_NAME = "ad_campaign_name"
    const val AD_SIZE = "ad_size"
    const val AD_GROUP_NAME = "ad_group_name"
    const val AD_GROUP_ID = "ad_group_id"
    const val AD_NETWORK_NAME = "ad_network_name"
    const val AD_MEDIATION_NAME = "ad_mediation_name"
    const val CHECKOUT_AS_GUEST = "checkout_as_guest"
    const val CONTENT_ID = "content_id"
    const val CONTENT_TYPE = "content_type"
    const val CURRENCY = "currency"
    const val DATE = "date"
    const val DESCRIPTION = "description"
    const val DESTINATION = "destination"
    const val DURATION = "duration"
    const val END_DATE = "end_date"
    const val ITEM_ADDED_FROM = "item_added_from"
    const val LEVEL = "level"
    const val MAX_RATING_VALUE = "max_rating_value"
    const val NAME = "name"
    const val ORDER_ID = "order_id"
    const val ORIGIN = "origin"
    const val PRICE = "price"
    const val QUANTITY = "quantity"
    const val RATING_VALUE = "rating_value"
    const val RECEIPT_ID = "receipt_id"
    const val REFERRAL_FROM = "referral_from"
    const val REGISTRATION_METHOD = "registration_method"
    const val RESULTS = "results"
    const val SCORE = "score"
    const val SEARCH_TERM = "search_term"
    const val START_DATE = "start_date"
    const val SUCCESS = "success"
    const val USER_ID = "user_id"
    const val USER_NAME = "user_name"
    const val VALIDATED = "validated"
    const val SPATIAL_X = "spatial_x"
    const val SPATIAL_Y = "spatial_y"
    const val SPATIAL_Z = "spatial_z"

    const val BACKGROUND = "background"
    const val ACTION = "action"
    const val COMPLETED = "completed"
    const val URI = "uri"
    const val SOURCE = "source"
}