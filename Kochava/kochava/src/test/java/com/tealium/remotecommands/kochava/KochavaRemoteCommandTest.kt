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
        purchaseProperties.put(Parameters.USER_ID, "10")
        purchaseProperties.put(Parameters.PRICE, 10.99)

        every { mockTracker.purchase(any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), purchaseProperties)
        verify {
            mockTracker.purchase("10", "", "", 10.99, "", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun purchaseCalledWithCustomParameters() {
        val parameters = JSONObject()
        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        parameters.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), parameters)

        verify {
            mockTracker.customEvent("Purchase", parameters)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithStandardParameters() {
        val tutorialProperties = JSONObject()
        tutorialProperties.put(Parameters.USER_ID, "10")
        tutorialProperties.put(Parameters.NAME, "tutorial")
        tutorialProperties.put(Parameters.DURATION, 10.50)

        every { mockTracker.tutorialLevelComplete(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), tutorialProperties)
        verify {
            mockTracker.tutorialLevelComplete("Tutorial Complete", "10", "tutorial", 10.50)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithCustomParameters() {
        val parameters = JSONObject()
        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        parameters.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), parameters)
        verify {
            mockTracker.customEvent("Tutorial Complete", parameters)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewWithStandardParameters() {
        val adviewProperties = JSONObject()
        adviewProperties.put(Parameters.AD_TYPE, "type")
        adviewProperties.put(Parameters.AD_NETWORK_NAME, "tutorial")
        adviewProperties.put(Parameters.PLACEMENT, "placement")
        adviewProperties.put(Parameters.AD_MEDIATION_NAME, "mediation")
        adviewProperties.put(Parameters.AD_CAMPAIGN_ID, "100")
        adviewProperties.put(Parameters.AD_CAMPAIGN_NAME, "campaign")
        adviewProperties.put(Parameters.AD_SIZE, "5")

        every { mockTracker.adview(any(), any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), adviewProperties)
        verify {
            mockTracker.adview("type", "tutorial", "placement", "mediation", "100", "campaogn", "5")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewCalledWithCustomParameters() {
        val adviewProperties = JSONObject()
        adviewProperties.put(Parameters.AD_TYPE, "type")

        val parameters = JSONObject()
        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)
        parameters.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), parameters)
        verify {
            mockTracker.customEvent("Ad View", adviewProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingWithStandardParameters() {
        val ratingProperties = JSONObject()
        ratingProperties.put(Parameters.RATING_VALUE, 5.3)
        ratingProperties.put(Parameters.MAX_RATING_VALUE, 10.0)

        every { mockTracker.rating(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), ratingProperties)
        verify {
            mockTracker.rating(5.3, 10.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingCalledWithCustomParameters() {
        val ratingProperties = JSONObject()
        ratingProperties.put(Parameters.RATING_VALUE, 3.2)

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)
        ratingProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), ratingProperties)
        verify {
            mockTracker.customEvent("Rating", ratingProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartWithStandardParameters() {
        val cartProperties = JSONObject()
        cartProperties.put(Parameters.USER_ID, "abc")
        cartProperties.put(Parameters.NAME, "name")
        cartProperties.put(Parameters.CONTENT_ID, "def")
        cartProperties.put(Parameters.CONTENT_TYPE, 3.0)

        every { mockTracker.addToCart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cartProperties)
        verify {
            mockTracker.addToCart("abc", "name", "def", 3.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartCalledWithCustomParameters() {
        val cartProperties = JSONObject()
        cartProperties.put(Parameters.REFERRAL_FORM, "form")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        cartProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cartProperties)
        verify {
            mockTracker.customEvent("Add To Cart", cartProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistWithStandardParameters() {
        val wishlistProperties = JSONObject()
        wishlistProperties.put(Parameters.USER_ID, "abc")
        wishlistProperties.put(Parameters.NAME, "name")
        wishlistProperties.put(Parameters.CONTENT_ID, "def")

        every { mockTracker.addToWishList(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlistProperties)
        verify {
            mockTracker.addToWishList("abc", "name", "def")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistCalledWithCustomParameters() {
        val wishlistProperties = JSONObject()
        wishlistProperties.put(Parameters.REFERRAL_FORM, "form")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        wishlistProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlistProperties)
        verify {
            mockTracker.customEvent("Add To Wishlist", wishlistProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutWithStandardParameters() {
        val checkoutProperties = JSONObject()
        checkoutProperties.put(Parameters.USER_ID, "abc")
        checkoutProperties.put(Parameters.CONTENT_ID, "def")

        every { mockTracker.checkoutStart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkoutProperties)
        verify {
            mockTracker.checkoutStart("abc", "", "def", "", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutCalledWithCustomParameters() {
        val checkoutProperties = JSONObject()
        checkoutProperties.put(Parameters.CONTENT_ID, "abc")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        checkoutProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkoutProperties)
        verify {
            mockTracker.customEvent("Checkout Start", checkoutProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchWithStandardParameters() {
        val searchProperties = JSONObject()
        searchProperties.put(Parameters.URI, "abc")

        every { mockTracker.search(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SEARCH), searchProperties)
        verify {
            mockTracker.search("abc", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchCalledWithCustomParameters() {
        val searchProperties = JSONObject()
        searchProperties.put(Parameters.RESULTS, "def")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        searchProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), searchProperties)
        verify {
            mockTracker.customEvent("Search", searchProperties)
        }
        confirmVerified(mockTracker)
    }


    @Test
    fun registerWithStandardParameters() {
        val registrationProperties = JSONObject()
        registrationProperties.put(Parameters.USER_ID, "abc")
        registrationProperties.put(Parameters.USER_NAME, "def")
        registrationProperties.put(Parameters.REFERRAL_FORM, "form")

        every { mockTracker.registrationComplete(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.REGISTRATION_COMPLETE), registrationProperties)
        verify {
            mockTracker.registrationComplete("abc", "def", "form")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun registerCalledWithCustomParameters() {
        val registrationProperties = JSONObject()
        registrationProperties.put(Parameters.USER_ID, "abc")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        registrationProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.REGISTRATION_COMPLETE), registrationProperties)
        verify {
            mockTracker.customEvent("Registration Complete", registrationProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewWithStandardParameters() {
        val viewProperties = JSONObject()
        viewProperties.put(Parameters.USER_ID, "abc")
        viewProperties.put(Parameters.NAME, "name")
        viewProperties.put(Parameters.CONTENT_ID, "def")

        every { mockTracker.view(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), viewProperties)
        verify {
            mockTracker.view("abc", "name", "def", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewCalledWithCustomParameters() {
        val viewProperties = JSONObject()
        viewProperties.put(Parameters.USER_ID, "abc")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        viewProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), viewProperties)
        verify {
            mockTracker.customEvent("View", viewProperties)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementWithStandardParameters() {
        val achievementProperties = JSONObject()
        achievementProperties.put(Parameters.USER_ID, "abc")
        achievementProperties.put(Parameters.NAME, "name")
        achievementProperties.put(Parameters.DURATION, 10.5)

        every { mockTracker.achievement(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievementProperties)
        verify {
            mockTracker.achievement("abc", "name", 10.5)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementCalledWithCustomParameters() {
        val achievementProperties = JSONObject()
        achievementProperties.put(Parameters.USER_ID, "abc")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        achievementProperties.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievementProperties)
        verify {
            mockTracker.customEvent("Achievement", achievementProperties)
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
