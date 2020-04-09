package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var purchaseBtn: Button
    private lateinit var tutorialBtn: Button
    private lateinit var levelBtn: Button
    private lateinit var adviewBtn: Button
    private lateinit var ratingBtn: Button
    private lateinit var cartBtn: Button
    private lateinit var wishlistBtn: Button
    private lateinit var checkoutBtn: Button
    private lateinit var searchBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var viewBtn: Button
    private lateinit var achievementBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var hashMap : HashMap<String, String>
                = HashMap<String, String> ()

        hashMap.put("name", "roya")

        println("Calling method")
        purchaseBtn = findViewById<Button>(R.id.purchaseBtn)
        purchaseBtn.setOnClickListener{ TealiumHelper.trackEvent("purchase", hashMap)}

        tutorialBtn = findViewById<Button>(R.id.tutorialBtn)
        tutorialBtn.setOnClickListener{ TealiumHelper.trackEvent("tutorial complete", hashMap)}

        levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.setOnClickListener{ TealiumHelper.trackEvent("level complete", hashMap)}

        adviewBtn = findViewById<Button>(R.id.adviewBtn)
        adviewBtn.setOnClickListener{ TealiumHelper.trackEvent("Ad View", hashMap)}

        ratingBtn = findViewById<Button>(R.id.ratingBtn)
        ratingBtn.setOnClickListener{ TealiumHelper.trackEvent("Rating", hashMap)}

        cartBtn = findViewById<Button>(R.id.cartBtn)
        cartBtn.setOnClickListener{ TealiumHelper.trackEvent("Add to Cart", hashMap)}

        wishlistBtn = findViewById<Button>(R.id.wishlistBtn)
        wishlistBtn.setOnClickListener{ TealiumHelper.trackEvent("Add to Wishlist", hashMap)}

        checkoutBtn = findViewById<Button>(R.id.checkoutBtn)
        checkoutBtn.setOnClickListener{ TealiumHelper.trackEvent("Checkout Start", hashMap)}

        searchBtn = findViewById<Button>(R.id.searchBtn)
        searchBtn.setOnClickListener{ TealiumHelper.trackEvent("Search", hashMap)}

        registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{ TealiumHelper.trackEvent("Registration Complete", hashMap)}

        viewBtn = findViewById<Button>(R.id.viewBtn)
        viewBtn.setOnClickListener{ TealiumHelper.trackEvent("View", hashMap)}

        achievementBtn = findViewById<Button>(R.id.achievementBtn)
        achievementBtn.setOnClickListener{ TealiumHelper.trackEvent("Achievement", hashMap)}
    }
}
