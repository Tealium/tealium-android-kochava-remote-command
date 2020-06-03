package com.tealium.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        purchaseBtn.setOnClickListener { TealiumHelper.trackEvent("purchase", emptyMap()) }

        tutorialBtn.setOnClickListener { TealiumHelper.trackEvent("complete_tutorial", emptyMap()) }

        levelBtn.setOnClickListener { TealiumHelper.trackEvent("levelcomplete", emptyMap()) }

        adviewBtn.setOnClickListener { TealiumHelper.trackEvent("adview", emptyMap()) }

        ratingBtn.setOnClickListener { TealiumHelper.trackEvent("rating", emptyMap()) }

        cartBtn.setOnClickListener { TealiumHelper.trackEvent("add_cart", emptyMap()) }

        wishlistBtn.setOnClickListener { TealiumHelper.trackEvent("add_wishlist", emptyMap()) }

        checkoutBtn.setOnClickListener { TealiumHelper.trackEvent("checkout", emptyMap()) }

        searchBtn.setOnClickListener { TealiumHelper.trackEvent("search", emptyMap()) }

        registerBtn.setOnClickListener { TealiumHelper.trackEvent("user_register", emptyMap()) }

        viewBtn.setOnClickListener { TealiumHelper.trackEvent("view", emptyMap()) }

        achievementBtn.setOnClickListener { TealiumHelper.trackEvent("achievement", emptyMap()) }

        customBtn.setOnClickListener { TealiumHelper.trackEvent("custom", emptyMap()) }
    }
}
