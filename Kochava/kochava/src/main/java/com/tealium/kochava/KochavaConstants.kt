package com.tealium.kochava

//@file:JvmName("KochavaConstants")

object Commands {
    const val COMMAND_KEY = "command_name"
    const val SEPARATOR = ","

    const val PURCHASE = "purchase"
    const val TUTORIAL_COMPLETE = "tutorialcomplete"
    const val LEVEL_COMPLETE = "levelcomplete"
    const val AD_VIEW = "adview"
    const val RATING = "rating"
    const val ADD_TO_CART = "addtocart"
    const val ADD_TO_WISH_LIST = "addtowishlist"
    const val CHECKOUT_START = "checkoutstart"
    const val SEARCH = "search"
    const val REGISTRATION_COMPLETE = "registrationcomplete"
    const val VIEW = "view"
    const val ACHIEVEMENT = "achievment"
}

object Purchase {
    const val PURCHASE_USER_ID = "kochava_purchase_user_id"
    const val PURCHASE_NAME = "kochava_purchase_currency"
    const val PURCHASE_CONTENT_ID = "kochava_purchase_content_id"
    const val PURCHASE_PRICE = "kochava_purchase_price"
    const val PURCHASE_CURRENCY = "kochava_purchase_currency"
    const val PURCHASE_CHECKOUT_AS_GUEST = "kochava_purchase_checkout_as_guest"
    const val PURCHASE_PARAMETERS = "kochava_purchase_parameters"
    const val PURCHASE = "purchase"
}


object TutorialComplete {
    const val TUTORIAL_USER_ID = "kochava_tutorial_user_id"
    const val TUTORIAL_NAME = "kochava_tutorial_name"
    const val TUTORIAL_DURATION = "kochava_tutorial_duration"
    const val TUTORIAL_PARAMETERS = "kochava_tutorial_parameters"
    const val TUTORIAL_COMPLETE = "tutorial_complete"
}

object LevelComplete {
    const val LEVEL_USER_ID = "kochava_level_user_id"
    const val LEVEL_NAME = "kochava_level_name"
    const val LEVEL_DURATION = "kochava_level_duration"
    const val LEVEL_PARAMETERS = "kochava_level_parameters"
    const val LEVEL_COMPLETE = "level_complete"
}

object AdView {
    const val ADVIEW_TYPE = "kochava_adview_type"
    const val ADVIEW_NETWORK_NAME = "kochava_adview_network_name"
    const val ADVIEW_PLACEMENT = "kochava_adview_placement"
    const val ADVIEW_MEDIATION_NAME = "kochava_adview_mediation_name"
    const val ADVIEW_CAMPAIGN_ID = "kochava_adview_campaign_id"
    const val ADVIEW_CAMPAIGN_NAME = "kochava_adview_campaign_name"
    const val ADVIEW_SIZE = "kochava_adview_size"
    const val ADVIEW_PARAMETERS = "kochava_adview_parameters"
    const val AD_VIEW = "ad_view"
}

object Rating {
    const val RATING_VALUE = "kochava_rating_value"
    const val RATING_MAXIMUM_RATING = "kochava_rating_maximum_rating"
    const val RATING_PARAMETERS = "kochava_rating_parameters"
    const val RATING = "rating"
}


object AddToCart {
    const val ADDTOCART_USER_ID = "kochava_addtocart_user_id"
    const val ADDTOCART_NAME = "kochava_addtocart_name"
    const val ADDTOCART_CONTENT_ID = "kochava_addtocart_content_id"
    const val ADDTOCART_QUANTITY = "kochava_addtocart_quantity"
    const val ADDTOCART_REFERRALFORM = "kochava_addtocart_referralform"
    const val ADDTOCART_PARAMETERS = "kochava_addtocart_parameters"
    const val ADD_TO_CART = "addtocart"
}

object AddToWishList {
    const val ADDTOWISHLIST_USER_ID = "kochava_addtowishlist_user_id"
    const val ADDTOWISHLIST_NAME = "kochava_addtowishlist_name"
    const val ADDTOWISHLIST_CONTENT_ID = "kochava_addtowishlist_content_id"
    const val ADDTOWISHLIST_REFERRALFORM = "kochava_addtowishlist_referralform"
    const val ADDTOWISHLIST_PARAMETERS = "kochava_addtowishlist_parameters"
    const val ADD_TO_WISH_LIST = "addtowishlist"
}

object CheckoutStart {
    const val CHECKOUT_START_USER_ID = "kochava_checkout_user_id"
    const val CHECKOUT_START_NAME = "kochava_checkout_name"
    const val CHECKOUT_START_CONTENT_ID = "kochava_checkout_content_id"
    const val CHECKOUT_START_CHECKOUT_AS_GUEST = "kochava_checkout_checkout_as_guest"
    const val CHECKOUT_START_CURRENCY = "kochava_checkout_currency"
    const val CHECKOUT_START_PARAMETERS = "kochava_checkout_parameters"
    const val CHECKOUT_START = "checkoutstart"
}

object Search {
    const val SEARCH_URI = "kochava_search_uri"
    const val SEARCH_RESULTS = "kochava_search_results"
    const val SEARCH_PARAMETERS = "kochava_search_parameters"
    const val SEARCH = "search"
}

object RegistrationComplete {
    const val REGISTRATION_COMPLETE_USER_ID = "kochava_registrationcomplete_user_id"
    const val REGISTRATION_COMPLETE_USER_NAME = "kochava_registrationcomplete_user_name"
    const val REGISTRATION_COMPLETE_REFERRALFORM = "kochava_registrationcomplete_referralform"
    const val REGISTRATION_COMPLETE_PARAMETERS = "kochava_registrationcomplete_parameters"
    const val REGISTRATION_COMPLETE = "registrationcomplete"
}

object View {
    const val VIEW_USER_ID = "kochava_view_user_id"
    const val VIEW_NAME = "kochava_view_name"
    const val VIEW_CONTENT_ID = "kochava_view_content_id"
    const val VIEW_REFERRAL_FORM = "kochava_view_referral_form"
    const val VIEW_PARAMETERS = "kochava_view_parameters"
    const val VIEW = "view"
}

object Achievement {
    const val ACHIEVEMENT_USER_ID = "kochava_achievement_user_id"
    const val ACHIEVEMENT_NAME = "kochava_achievement_name"
    const val ACHIEVEMENT_DURATION = "kochava_achievement_duration"
    const val ACHIEVEMENT_PARAMETERS = "kochava_achievement_parameters"
    const val ACHIEVEMENT= "achievement"
}