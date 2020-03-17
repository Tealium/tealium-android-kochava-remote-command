package com.tealium.remotecommands.kochava;


import android.app.Application
import com.tealium.kochava.*
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
public class KochavaRemoteCommandTest {

    @MockK
    lateinit var mockTracker: KochavaTrackable

    @MockK
    lateinit var mockApp: Application

    @InjectMockKs
    var kochavaRemoteCommand: KochavaRemoteCommand = KochavaRemoteCommand(mockApp, "123")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun splitCommands() {
        val json = JSONObject()
        json.put("command_name", "purchase, tutorialcomplete, checkoutstart")
        val commands = kochavaRemoteCommand.splitCommands(json)

        assertEquals(3, commands.count())
        assertEquals("purchase", commands[0])
        assertEquals("tutorialcomplete", commands[1])
        assertEquals("checkoutstart", commands[2])
    }

    @Test
    fun purchaseCalledWithStandardParameters() {
        val purchaseProperties = JSONObject()
        purchaseProperties.put(Purchase.PURCHASE_USER_ID, "10")
        purchaseProperties.put(Purchase.PURCHASE_PRICE, 10.99)

        val purchase = JSONObject()
        purchase.put(Purchase.PURCHASE, purchaseProperties)

        every { mockTracker.purchase(any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), purchase)
        verify {
            mockTracker.purchase("10", "", "", 10.99, "", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun purchaseCalledWithCustomParameters() {
        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        val purchase = JSONObject()
        purchase.put(Purchase.PURCHASE_PARAMETERS, parameters)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), purchase)

        verify {
            mockTracker.customEvent("Purchase", parameters)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithStandardParameters() {
        val tutorialProperties = JSONObject()
        tutorialProperties.put(TutorialComplete.TUTORIAL_USER_ID, "10")
        tutorialProperties.put(TutorialComplete.TUTORIAL_NAME, "tutorial")
        tutorialProperties.put(TutorialComplete.TUTORIAL_DURATION, 10.50)

        val tutorial = JSONObject()
        tutorial.put(TutorialComplete.TUTORIAL_COMPLETE, tutorialProperties)

        every { mockTracker.tutorialLevelComplete(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), tutorial)
        verify {
            mockTracker.tutorialLevelComplete("Tutorial Complete", "10", "tutorial", 10.50)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithCustomParameters() {
        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        val tutorial = JSONObject()
        tutorial.put(TutorialComplete.TUTORIAL_PARAMETERS, parameters)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), tutorial)
        verify {
            mockTracker.customEvent("Tutorial Complete", parameters)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewWithStandardParameters() {
        val adviewProperties = JSONObject()
        adviewProperties.put(AdView.ADVIEW_TYPE, "type")
        adviewProperties.put(AdView.ADVIEW_NETWORK_NAME, "tutorial")
        adviewProperties.put(AdView.ADVIEW_PLACEMENT, "placement")
        adviewProperties.put(AdView.ADVIEW_MEDIATION_NAME, "mediation")
        adviewProperties.put(AdView.ADVIEW_CAMPAIGN_ID, "100")
        adviewProperties.put(AdView.ADVIEW_CAMPAIGN_NAME, "campaign")
        adviewProperties.put(AdView.ADVIEW_SIZE, "5")

        val adview = JSONObject()
        adview.put(AdView.AD_VIEW, adviewProperties)

        every { mockTracker.adview(any(), any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), adview)
        verify {
            mockTracker.adview("type", "tutorial", "placement", "mediation", "100", "campaogn", "5")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewCalledWithCustomParameters() {
        val adviewProperties = JSONObject()
        adviewProperties.put(AdView.ADVIEW_TYPE, "type")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        adviewProperties.put(AdView.ADVIEW_PARAMETERS, parameters)

        val adview = JSONObject()
        adview.put(AdView.AD_VIEW, adviewProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), adview)
        verify {
            mockTracker.customEvent("Ad View", adviewProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingWithStandardParameters() {
        val ratingProperties = JSONObject()
        ratingProperties.put(Rating.RATING_VALUE, 5.3)
        ratingProperties.put(Rating.RATING_MAXIMUM_RATING, 10.0)

        val rating = JSONObject()
        rating.put(Rating.RATING, ratingProperties)

        every { mockTracker.rating(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), rating)
        verify {
            mockTracker.rating(5.3, 10.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingCalledWithCustomParameters() {
        val ratingProperties = JSONObject()
        ratingProperties.put(Rating.RATING_VALUE, 3.2)

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        ratingProperties.put(Rating.RATING_PARAMETERS, parameters)

        val rating = JSONObject()
        rating.put(Rating.RATING, ratingProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), rating)
        verify {
            mockTracker.customEvent("Rating", ratingProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartWithStandardParameters() {
        val cartProperties = JSONObject()
        cartProperties.put(AddToCart.ADDTOCART_USER_ID, "abc")
        cartProperties.put(AddToCart.ADDTOCART_NAME, "name")
        cartProperties.put(AddToCart.ADDTOCART_CONTENT_ID, "def")
        cartProperties.put(AddToCart.ADDTOCART_QUANTITY, 3.0)

        val cart = JSONObject()
        cart.put(AddToCart.ADD_TO_CART, cartProperties)

        every { mockTracker.addToCart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cart)
        verify {
            mockTracker.addToCart("abc", "name", "def", 3.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartCalledWithCustomParameters() {
        val cartProperties = JSONObject()
        cartProperties.put(AddToCart.ADDTOCART_REFERRALFORM, "form")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        cartProperties.put(AddToCart.ADDTOCART_PARAMETERS, parameters)

        val cart = JSONObject()
        cart.put(AddToCart.ADD_TO_CART, cartProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cart)
        verify {
            mockTracker.customEvent("Add To Cart", cart)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistWithStandardParameters() {
        val wishlistProperties = JSONObject()
        wishlistProperties.put(AddToWishList.ADDTOWISHLIST_USER_ID, "abc")
        wishlistProperties.put(AddToWishList.ADDTOWISHLIST_NAME, "name")
        wishlistProperties.put(AddToWishList.ADDTOWISHLIST_CONTENT_ID, "def")

        val wishlist = JSONObject()
        wishlist.put(AddToWishList.ADD_TO_WISH_LIST, wishlistProperties)

        every { mockTracker.addToWishList(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlist)
        verify {
            mockTracker.addToWishList("abc", "name", "def")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistCalledWithCustomParameters() {
        val wishlistProperties = JSONObject()
        wishlistProperties.put(AddToCart.ADDTOCART_REFERRALFORM, "form")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        wishlistProperties.put(AddToWishList.ADDTOWISHLIST_PARAMETERS, parameters)

        val wishlist = JSONObject()
        wishlist.put(AddToWishList.ADD_TO_WISH_LIST, wishlistProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlist)
        verify {
            mockTracker.customEvent("Add To Wishlist", wishlist)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutWithStandardParameters() {
        val checkoutProperties = JSONObject()
        checkoutProperties.put(CheckoutStart.CHECKOUT_START_USER_ID, "abc")
        checkoutProperties.put(CheckoutStart.CHECKOUT_START_CONTENT_ID, "def")

        val checkout = JSONObject()
        checkout.put(CheckoutStart.CHECKOUT_START, checkoutProperties)

        every { mockTracker.checkoutStart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkout)
        verify {
            mockTracker.checkoutStart("abc", "", "def", "", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutCalledWithCustomParameters() {
        val checkoutProperties = JSONObject()
        checkoutProperties.put(CheckoutStart.CHECKOUT_START_CONTENT_ID, "abc")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        checkoutProperties.put(CheckoutStart.CHECKOUT_START_PARAMETERS, parameters)

        val checkout = JSONObject()
        checkout.put(CheckoutStart.CHECKOUT_START, checkoutProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkout)
        verify {
            mockTracker.customEvent("Checkout Start", checkout)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchWithStandardParameters() {
        val searchProperties = JSONObject()
        searchProperties.put(Search.SEARCH_URI, "abc")

        val search = JSONObject()
        search.put(CheckoutStart.CHECKOUT_START, searchProperties)

        every { mockTracker.search(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SEARCH), search)
        verify {
            mockTracker.search("abc", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchCalledWithCustomParameters() {
        val searchProperties = JSONObject()
        searchProperties.put(Search.SEARCH_RESULTS, "def")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        searchProperties.put(Search.SEARCH_PARAMETERS, parameters)

        val search = JSONObject()
        search.put(Search.SEARCH, searchProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), search)
        verify {
            mockTracker.customEvent("Search", search)
        }
        confirmVerified(mockTracker)
    }


    @Test
    fun registerWithStandardParameters() {
        val registrationProperties = JSONObject()
        registrationProperties.put(RegistrationComplete.REGISTRATION_COMPLETE_USER_ID, "abc")
        registrationProperties.put(RegistrationComplete.REGISTRATION_COMPLETE_USER_NAME, "def")
        registrationProperties.put(RegistrationComplete.REGISTRATION_COMPLETE_REFERRALFORM, "form")

        val registration = JSONObject()
        registration.put(RegistrationComplete.REGISTRATION_COMPLETE, registrationProperties)

        every { mockTracker.registrationComplete(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.REGISTRATION_COMPLETE), registration)
        verify {
            mockTracker.registrationComplete("abc", "def", "form")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun registerCalledWithCustomParameters() {
        val registrationProperties = JSONObject()
        registrationProperties.put(RegistrationComplete.REGISTRATION_COMPLETE_USER_ID, "abc")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        registrationProperties.put(RegistrationComplete.REGISTRATION_COMPLETE_PARAMETERS, parameters)

        val registration = JSONObject()
        registration.put(RegistrationComplete.REGISTRATION_COMPLETE, registrationProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.REGISTRATION_COMPLETE), registration)
        verify {
            mockTracker.customEvent("Registration Complete", registration)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewWithStandardParameters() {
        val viewProperties = JSONObject()
        viewProperties.put(View.VIEW_USER_ID, "abc")
        viewProperties.put(View.VIEW_NAME, "name")
        viewProperties.put(View.VIEW_CONTENT_ID, "def")

        val view = JSONObject()
        view.put(View.VIEW, viewProperties)

        every { mockTracker.view(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), view)
        verify {
            mockTracker.view("abc", "name", "def", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewCalledWithCustomParameters() {
        val viewProperties = JSONObject()
        viewProperties.put(View.VIEW_USER_ID, "abc")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        viewProperties.put(View.VIEW_PARAMETERS, parameters)

        val view = JSONObject()
        view.put(View.VIEW, viewProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), view)
        verify {
            mockTracker.customEvent("View", view)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementWithStandardParameters() {
        val achievementProperties = JSONObject()
        achievementProperties.put(Achievement.ACHIEVEMENT_USER_ID, "abc")
        achievementProperties.put(Achievement.ACHIEVEMENT_NAME, "name")
        achievementProperties.put(Achievement.ACHIEVEMENT_DURATION, 10.5)

        val achievement = JSONObject()
        achievement.put(Achievement.ACHIEVEMENT, achievementProperties)

        every { mockTracker.achievement(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievement)
        verify {
            mockTracker.achievement("abc", "name", 10.5)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementCalledWithCustomParameters() {
        val achievementProperties = JSONObject()
        achievementProperties.put(Achievement.ACHIEVEMENT_USER_ID, "abc")

        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        achievementProperties.put(Achievement.ACHIEVEMENT_PARAMETERS, parameters)

        val achievement = JSONObject()
        achievement.put(Achievement.ACHIEVEMENT, achievementProperties)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievement)
        verify {
            mockTracker.customEvent("Achievement", achievement)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun customEventAndParameters() {
        val parameters = JSONObject()
        parameters.put("key1", "value1")
        parameters.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        verify {
            mockTracker.customEvent("Any Event Name", parameters)
        }
        confirmVerified(mockTracker)
    }

}
