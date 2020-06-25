package com.tealium.remotecommands.kochava;

import Commands
import android.app.Application
import android.content.Context
import com.tealium.kochava.KochavaRemoteCommand
import com.tealium.kochava.KochavaTrackable
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
class KochavaRemoteCommandTest {

    @MockK
    lateinit var mockTracker: KochavaTrackable

    @MockK
    lateinit var mockApp: Application

    @InjectMockKs
    var kochavaRemoteCommand: KochavaRemoteCommand = KochavaRemoteCommand(null, "123")

    @MockK
    lateinit var mockapplicationContext: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockApp.applicationContext } returns mockapplicationContext
    }

    @Test
    fun splitCommands() {
        val json = JSONObject()
        json.put("command_name", "Purchase,TutorialComplete,CheckoutStart")
        val commands = kochavaRemoteCommand.splitCommands(json)

        assertEquals(3, commands.count())
        assertEquals("purchase", commands[0])
        assertEquals("tutorialcomplete", commands[1])
        assertEquals("checkoutstart", commands[2])
    }

    @Test
    fun purchaseCalledWithStandardParameters() {
        val purchaseProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "10")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "100")
        eventParams.put(EventParameters.PRICE, 10.99)
        eventParams.put(EventParameters.CURRENCY, "usd")
        eventParams.put(EventParameters.CHECKOUT_AS_GUEST, "yes")

        purchaseProperties.put("event_parameters", eventParams)

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
        val eventParams = JSONObject()
        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.PURCHASE), parameters)

        verify {
            mockTracker.customEvent(Commands.PURCHASE, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithStandardParameters() {
        val tutorialProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "10")
        eventParams.put(EventParameters.NAME, "tutorial")
        eventParams.put(EventParameters.DURATION, 10.50)

        tutorialProperties.put("event_parameters", eventParams)

        every { mockTracker.tutorialComplete(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), tutorialProperties)
        verify {
            mockTracker.tutorialComplete("10", "tutorial", 10.50)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun tutorialCompleteCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.TUTORIAL_COMPLETE), parameters)
        verify {
            mockTracker.customEvent(Commands.TUTORIAL_COMPLETE, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun levelCompleteCalledWithStandardParameters() {
        val tutorialProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "10")
        eventParams.put(EventParameters.NAME, "level")

        tutorialProperties.put("event_parameters", eventParams)

        every { mockTracker.levelComplete(any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.LEVEL_COMPLETE), tutorialProperties)
        verify {
            mockTracker.levelComplete("10", "level", Double.NaN)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun levelCompleteCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.LEVEL_COMPLETE), parameters)
        verify {
            mockTracker.customEvent(Commands.LEVEL_COMPLETE, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewWithStandardParameters() {
        val adviewProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.AD_TYPE, "type")
        eventParams.put(EventParameters.AD_NETWORK_NAME, "tutorial")
        eventParams.put(EventParameters.PLACEMENT, "placement")
        eventParams.put(EventParameters.AD_MEDIATION_NAME, "mediation")
        eventParams.put(EventParameters.AD_CAMPAIGN_ID, "100")
        eventParams.put(EventParameters.AD_CAMPAIGN_NAME, "campaign")
        eventParams.put(EventParameters.AD_SIZE, "5")

        adviewProperties.put("event_parameters", eventParams)

        every { mockTracker.adView(any(), any(), any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), adviewProperties)
        verify {
            mockTracker.adView("type", "tutorial", "placement", "mediation", "100", "campaign", "5")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun adViewCalledWithCustomParameters() {
        val eventParams = JSONObject()
        eventParams.put(EventParameters.AD_TYPE, "type")
        eventParams.put(EventParameters.AD_NETWORK_NAME, "tutorial")
        eventParams.put(EventParameters.PLACEMENT, "placement")
        eventParams.put(EventParameters.AD_MEDIATION_NAME, "mediation")
        eventParams.put(EventParameters.AD_CAMPAIGN_ID, "100")
        eventParams.put(EventParameters.AD_CAMPAIGN_NAME, "campaign")
        eventParams.put(EventParameters.AD_SIZE, "5")

        val parameters = JSONObject()
        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.AD_TYPE, "type")
        infoDictionary.put(EventParameters.AD_NETWORK_NAME, "tutorial")
        infoDictionary.put(EventParameters.PLACEMENT, "placement")
        infoDictionary.put(EventParameters.AD_MEDIATION_NAME, "mediation")
        infoDictionary.put(EventParameters.AD_CAMPAIGN_ID, "100")
        infoDictionary.put(EventParameters.AD_CAMPAIGN_NAME, "campaign")
        infoDictionary.put(EventParameters.AD_SIZE, "5")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.AD_VIEW), parameters)
        verify {
            mockTracker.customEvent(Commands.AD_VIEW, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingWithStandardParameters() {
        val ratingProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.RATING_VALUE, 5.3)
        eventParams.put(EventParameters.MAX_RATING_VALUE, 10.0)

        ratingProperties.put("event_parameters", eventParams)

        every { mockTracker.rating(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), ratingProperties)
        verify {
            mockTracker.rating(5.3, 10.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun ratingCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.RATING_VALUE, 3.2)

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.RATING_VALUE, 3.2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.RATING), parameters)
        verify {
            mockTracker.customEvent(Commands.RATING, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartWithStandardParameters() {
        val cartProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")
        eventParams.put(EventParameters.QUANTITY, 3.0)

        cartProperties.put("event_parameters", eventParams)

        every { mockTracker.addToCart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), cartProperties)
        verify {
            mockTracker.addToCart("abc", "name", "def", 3.0)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToCartCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.REFERRAL_FROM, "referral")
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.REFERRAL_FROM, "referral")
        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.CONTENT_ID, "def")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_CART), parameters)
        verify {
            mockTracker.customEvent(Commands.ADD_TO_CART, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistWithStandardParameters() {
        val wishlistProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")

        wishlistProperties.put("event_parameters", eventParams)

        every { mockTracker.addToWishList(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), wishlistProperties)
        verify {
            mockTracker.addToWishList("abc", "name", "def")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun addToWishlistCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.REFERRAL_FROM, "referral")
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.REFERRAL_FROM, "referral")
        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.CONTENT_ID, "def")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ADD_TO_WISH_LIST), parameters)
        verify {
            mockTracker.customEvent(Commands.ADD_TO_WISH_LIST, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutWithStandardParameters() {
        val checkoutProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.CONTENT_ID, "def")

        checkoutProperties.put("event_parameters", eventParams)

        every { mockTracker.checkoutStart(any(), any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), checkoutProperties)
        verify {
            mockTracker.checkoutStart("abc", "", "def")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun checkoutCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.CONTENT_ID, "abc")
        eventParams.put(EventParameters.CHECKOUT_AS_GUEST, "yes")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CURRENCY, "usd")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.CONTENT_ID, "abc")
        infoDictionary.put(EventParameters.CHECKOUT_AS_GUEST, "yes")
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.CURRENCY, "usd")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.CHECKOUT_START), parameters)
        verify {
            mockTracker.customEvent(Commands.CHECKOUT_START, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchWithStandardParameters() {
        val searchProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.URI, "abc")

        searchProperties.put("event_parameters", eventParams)

        every { mockTracker.search(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SEARCH), searchProperties)
        verify {
            mockTracker.search("abc", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun searchCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.URI, "abc")
        eventParams.put(EventParameters.RESULTS, "def")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.URI, "abc")
        infoDictionary.put(EventParameters.RESULTS, "def")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SEARCH), parameters)
        verify {
            mockTracker.customEvent(Commands.SEARCH, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }


    @Test
    fun registerWithStandardParameters() {
        val registrationProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.USER_NAME, "def")
        eventParams.put(EventParameters.REFERRAL_FROM, "referral")

        registrationProperties.put("event_parameters", eventParams)

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
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.USER_NAME, "def")
        eventParams.put(EventParameters.REFERRAL_FROM, "referral")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.USER_NAME, "def")
        infoDictionary.put(EventParameters.REFERRAL_FROM, "referral")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(
            arrayOf(Commands.REGISTRATION_COMPLETE),
            parameters
        )
        verify {
            mockTracker.customEvent(Commands.REGISTRATION_COMPLETE, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewWithStandardParameters() {
        val viewProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")

        viewProperties.put("event_parameters", eventParams)

        every { mockTracker.view(any(), any(), any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), viewProperties)
        verify {
            mockTracker.view("abc", "name", "def", "")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun viewCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CONTENT_ID, "def")
        eventParams.put(EventParameters.REFERRAL_FROM, "ref")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.CONTENT_ID, "def")
        infoDictionary.put(EventParameters.REFERRAL_FROM, "ref")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.VIEW), parameters)
        verify {
            mockTracker.customEvent(Commands.VIEW, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementWithStandardParameters() {
        val achievementProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")

        achievementProperties.put("event_parameters", eventParams)

        every { mockTracker.achievement(any(), any(), any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), achievementProperties)
        verify {
            mockTracker.achievement("abc", "name", Double.NaN)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun achievementCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.NAME, "name")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.ACHIEVEMENT), parameters)
        verify {
            mockTracker.customEvent(Commands.ACHIEVEMENT, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }


    @Test
    fun deepLinkWithStandardParameters() {
        val deepLinkProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.URI, "abc")

        deepLinkProperties.put("event_parameters", eventParams)

        every { mockTracker.deepLink(any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.DEEP_LINK), deepLinkProperties)
        verify {
            mockTracker.deepLink("abc")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun deepLinkCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.USER_ID, "abc")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.DEEP_LINK), parameters)
        verify {
            mockTracker.customEvent(Commands.DEEP_LINK, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun subscribeWithStandardParameters() {
        val subscribeProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CURRENCY, "USD")

        subscribeProperties.put("event_parameters", eventParams)

        every { mockTracker.subscribe(any(), any(), any(), any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SUBSCRIBE), subscribeProperties)
        verify {
            mockTracker.subscribe(Double.NaN, "USD", "name", "abc")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun subscribeCalledWithCustomParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.PRICE, 9.0)
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.CURRENCY, "USD")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.PRICE, 9.0)
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.CURRENCY, "USD")

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.SUBSCRIBE), parameters)
        verify {
            mockTracker.customEvent(Commands.SUBSCRIBE, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun startTrialWithStandardParameters() {
        val trialProperties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CURRENCY, "USD")

        trialProperties.put("event_parameters", eventParams)

        every { mockTracker.startTrial(any(), any(), any(), any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.START_TRIAL), trialProperties)
        verify {
            mockTracker.startTrial(Double.NaN, "USD", "name", "abc")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun startTrialCalledWithCustomParameters() {
        val eventParams = JSONObject()
        val parameters = JSONObject()
        eventParams.put(EventParameters.PRICE, 9.0)
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.CURRENCY, "USD")

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.PRICE, 9.0)
        infoDictionary.put(EventParameters.NAME, "name")
        infoDictionary.put(EventParameters.USER_ID, "abc")
        infoDictionary.put(EventParameters.CURRENCY, "USD")
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.START_TRIAL), parameters)
        verify {
            mockTracker.customEvent(Commands.START_TRIAL, infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun customEventAndParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.BACKGROUND, false)
        eventParams.put(EventParameters.COMPLETED, false)

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary)
        parameters.put("event_parameters", eventParams)

        infoDictionary.put(EventParameters.BACKGROUND, false)
        infoDictionary.put(EventParameters.COMPLETED, false)
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("Any Event Name"), parameters)
        verify {
            mockTracker.customEvent("Any Event Name", infoDictionary.toString())
        }
        confirmVerified(mockTracker)
    }

}
