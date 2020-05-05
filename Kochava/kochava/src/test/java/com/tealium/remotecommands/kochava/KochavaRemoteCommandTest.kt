package com.tealium.remotecommands.kochava;

import Commands
import Parameters
import android.app.Application
import android.content.Context
import com.tealium.kochava.KochavaRemoteCommand
import com.tealium.kochava.KochavaTrackable
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class KochavaRemoteCommandTest {

    @MockK
    lateinit var mockTracker: KochavaTrackable

    @MockK
    lateinit var mockApp: Application

    lateinit var kochavaRemoteCommand: KochavaRemoteCommand

    @MockK
    lateinit var mockapplicationContext: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockApp.applicationContext } returns mockapplicationContext
        kochavaRemoteCommand = KochavaRemoteCommand(mockApp, "123", tracker = mockTracker)
    }

    @Test
    fun splitCommands() {
        val json = JSONObject()
        json.put("command_name", "Purchase,Tutorial Complete,Checkout Start")
        val commands = kochavaRemoteCommand.splitCommands(json)

        assertEquals(3, commands.count())
        assertEquals("purchase", commands[0])
        assertEquals("tutorial complete", commands[1])
        assertEquals("checkout start", commands[2])
    }

    @Test
    fun purchaseCalledWithStandardParameters() {
        val purchaseProperties = JSONObject()
        purchaseProperties.put(Parameters.USER_ID, "10")
        purchaseProperties.put(Parameters.NAME, "name")
        purchaseProperties.put(Parameters.CONTENT_ID, "100")
        purchaseProperties.put(Parameters.PRICE, 10.99)
        purchaseProperties.put(Parameters.CURRENCY, "usd")
        purchaseProperties.put(Parameters.CHECKOUT_AS_GUEST, "yes")

        every { mockTracker.purchase(any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), purchaseProperties)
        verify {
            mockTracker.purchase("10", "name", "100", 10.99, "usd", "yes")
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
            mockTracker.customEvent(Commands.PURCHASE, info_dictionary.toString())
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
            mockTracker.tutorialLevelComplete(Commands.TUTORIAL_COMPLETE, "10", "tutorial", 10.50)
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
            mockTracker.customEvent(Commands.TUTORIAL_COMPLETE, info_dictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun levelCompleteCalledWithStandardParameters() {
        val tutorialProperties = JSONObject()
        tutorialProperties.put(Parameters.USER_ID, "10")
        tutorialProperties.put(Parameters.NAME, "level")
        tutorialProperties.put(Parameters.DURATION, 10.50)

        every { mockTracker.tutorialLevelComplete(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.LEVEL_COMPLETE), tutorialProperties)
        verify {
            mockTracker.tutorialLevelComplete(Commands.LEVEL_COMPLETE, "10", "level", 10.50)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun levelCompleteCalledWithCustomParameters() {
        val parameters = JSONObject()
        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)

        parameters.put("info_dictionary", info_dictionary)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.LEVEL_COMPLETE), parameters)
        verify {
            mockTracker.customEvent(Commands.LEVEL_COMPLETE, info_dictionary.toString())
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

        every { mockTracker.adView(any(), any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), adviewProperties)
        verify {
            mockTracker.adView("type", "tutorial", "placement", "mediation", "100", "campaign", "5")
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
            mockTracker.customEvent(Commands.AD_VIEW, info_dictionary.toString())
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.RATING_VALUE, 3.2)
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), ratingProperties)
        verify {
            mockTracker.customEvent(Commands.RATING, mergedParams.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartWithStandardParameters() {
        val cartProperties = JSONObject()
        cartProperties.put(Parameters.USER_ID, "abc")
        cartProperties.put(Parameters.NAME, "name")
        cartProperties.put(Parameters.CONTENT_ID, "def")
        cartProperties.put(Parameters.QUANTITY, 3.0)

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
        cartProperties.put(Parameters.REFERRAL_FROM, "referral")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)
        cartProperties.put("info_dictionary", info_dictionary)

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.REFERRAL_FROM, "referral")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cartProperties)
        verify {
            mockTracker.customEvent(Commands.ADD_TO_CART, mergedParams.toString())
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
        wishlistProperties.put(Parameters.REFERRAL_FROM, "referral")

        val info_dictionary = JSONObject()
        info_dictionary.put("key1", "value1")
        info_dictionary.put("key2", 2)
        wishlistProperties.put("info_dictionary", info_dictionary)

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.REFERRAL_FROM, "referral")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlistProperties)
        verify {
            mockTracker.customEvent(Commands.ADD_TO_WISH_LIST, mergedParams.toString())
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.CONTENT_ID, "abc")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkoutProperties)
        verify {
            mockTracker.customEvent(Commands.CHECKOUT_START, mergedParams.toString())
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.RESULTS, "def")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SEARCH), searchProperties)
        verify {
            mockTracker.customEvent(Commands.SEARCH, mergedParams.toString())
        }
        confirmVerified(mockTracker)
    }


    @Test
    fun registerWithStandardParameters() {
        val registrationProperties = JSONObject()
        registrationProperties.put(Parameters.USER_ID, "abc")
        registrationProperties.put(Parameters.USER_NAME, "def")
        registrationProperties.put(Parameters.REFERRAL_FROM, "referral")

        every { mockTracker.registrationComplete(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(
            arrayOf(Commands.REGISTRATION_COMPLETE),
            registrationProperties
        )
        verify {
            mockTracker.registrationComplete("abc", "def", "referral")
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.USER_ID, "abc")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(
            arrayOf(Commands.REGISTRATION_COMPLETE),
            registrationProperties
        )
        verify {
            mockTracker.customEvent(Commands.REGISTRATION_COMPLETE, mergedParams.toString())
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.USER_ID, "abc")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), viewProperties)
        verify {
            mockTracker.customEvent(Commands.VIEW, mergedParams.toString())
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

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.USER_ID, "abc")
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievementProperties)
        verify {
            mockTracker.customEvent(Commands.ACHIEVEMENT, mergedParams.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun customEventAndParameters() {
//        val parameters = JSONObject()
//        parameters.put("key1", "value1")
//        parameters.put("key2", 2)
//        parameters.put("user_id", "100")
//        parameters.put(Parameters.BACKGROUND, "false")
//        parameters.put(Parameters.COMPLETED, "false")

        val standardParams = JSONObject()
        standardParams.put(Parameters.BACKGROUND, false)
        standardParams.put(Parameters.COMPLETED, false)

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)
        standardParams.put("info_dictionary", infoDictionary)

        val mergedParams = JSONObject()
        mergedParams.put(Parameters.BACKGROUND, false)
        mergedParams.put(Parameters.COMPLETED, false)
        mergedParams.put("key1", "value1")
        mergedParams.put("key2", 2)

//        val mergedParams = kochavaRemoteCommand.mergeJSONObjects(infoDictionary, standardParams)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("Any Event Name"), standardParams)
        verify {
            mockTracker.customEvent("Any Event Name", mergedParams.toString())
        }
        confirmVerified(mockTracker)
    }

}
