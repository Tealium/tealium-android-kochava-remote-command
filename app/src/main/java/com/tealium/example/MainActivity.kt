package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tealium.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginBtn.setOnClickListener { TealiumHelper.trackEvent("user_login", emptyMap()) }
        binding.registerBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "user_register",
                emptyMap()
            )
        }
        binding.searchBtn.setOnClickListener { TealiumHelper.trackEvent("search", emptyMap()) }
        binding.viewBtn.setOnClickListener {
            TealiumHelper.trackView(
                "view",
                mapOf("campaign" to "Test Ad", "campaign_id" to "tester123")
            )
        }
        binding.cartBtn.setOnClickListener { TealiumHelper.trackEvent("cart_add", emptyMap()) }
        binding.wishlistBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "wishlist_add",
                emptyMap()
            )
        }
        binding.checkoutBtn.setOnClickListener { TealiumHelper.trackEvent("checkout", emptyMap()) }
        binding.purchaseBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "order",
                mapOf(
                    "order_name" to "Special Purchase order",
                    "currency_code" to "usd",
                    "product_unit_price" to 5,
                )
            )
        }
        binding.ratingBtn.setOnClickListener { TealiumHelper.trackEvent("rating", emptyMap()) }
        binding.tutorialBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "stop_tutorial",
                emptyMap()
            )
        }
        binding.levelBtn.setOnClickListener { TealiumHelper.trackEvent("level_up", emptyMap()) }
        binding.achievementBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "unlock_achievement",
                mapOf("achievement_id" to "Top Score")
            )
        }
        binding.customBtn.setOnClickListener {
            TealiumHelper.trackEvent(
                "record_score",
                mapOf("custom_name" to "record_score", "testKey" to "testValue")
            )
        }
    }
}