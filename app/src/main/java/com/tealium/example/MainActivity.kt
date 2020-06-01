package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

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
    private lateinit var customBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var hashMap : HashMap<String, String> = HashMap<String, String>()

        hashMap.put("name", "roya")

        purchaseBtn = findViewById<Button>(R.id.purchaseBtn)
        purchaseBtn.setOnClickListener{ TealiumHelper.trackEvent("purchase", hashMap)}

        tutorialBtn = findViewById<Button>(R.id.tutorialBtn)
        tutorialBtn.setOnClickListener{ TealiumHelper.trackEvent("complete_tutorial", hashMap)}

        levelBtn = findViewById<Button>(R.id.levelBtn)
        levelBtn.setOnClickListener{ TealiumHelper.trackEvent("levelcomplete", hashMap)}

        adviewBtn = findViewById<Button>(R.id.adviewBtn)
        adviewBtn.setOnClickListener{ TealiumHelper.trackEvent("adview", hashMap)}

        ratingBtn = findViewById<Button>(R.id.ratingBtn)
        ratingBtn.setOnClickListener{ TealiumHelper.trackEvent("rating", hashMap)}

        cartBtn = findViewById<Button>(R.id.cartBtn)
        cartBtn.setOnClickListener{ TealiumHelper.trackEvent("add_cart", hashMap)}

        wishlistBtn = findViewById<Button>(R.id.wishlistBtn)
        wishlistBtn.setOnClickListener{ TealiumHelper.trackEvent("add_wishlist", hashMap)}

        checkoutBtn = findViewById<Button>(R.id.checkoutBtn)
        checkoutBtn.setOnClickListener{ TealiumHelper.trackEvent("checkout", hashMap)}

        searchBtn = findViewById<Button>(R.id.searchBtn)
        searchBtn.setOnClickListener{ TealiumHelper.trackEvent("search", hashMap)}

        registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{ TealiumHelper.trackEvent("user_register", hashMap)}

        viewBtn = findViewById<Button>(R.id.viewBtn)
        viewBtn.setOnClickListener{ TealiumHelper.trackEvent("view", hashMap)}

        achievementBtn = findViewById<Button>(R.id.achievementBtn)
        achievementBtn.setOnClickListener{ TealiumHelper.trackEvent("achievement", hashMap)}

        customBtn = findViewById<Button>(R.id.customBtn)
        customBtn.setOnClickListener{ TealiumHelper.trackEvent("custom", hashMap)}
    }
}
