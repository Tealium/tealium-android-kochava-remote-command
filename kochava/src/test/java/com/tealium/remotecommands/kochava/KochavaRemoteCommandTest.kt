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
    fun customEventAndParameters() {
        val parameters = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.BACKGROUND, false)
        eventParams.put(EventParameters.COMPLETED, false)

        val infoDictionary = JSONObject()
        infoDictionary.put("key1", "value1")
        infoDictionary.put("key2", 2)

        eventParams.put("info_dictionary", infoDictionary.toString())
        parameters.put("event_parameters", eventParams)

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("Any Event Name"), parameters)
        verify {
            mockTracker.customEvent("Any Event Name", eventParams.toString())
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun eventWithStandardParameters() {
        val properties = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CURRENCY, "USD")
        eventParams.put(EventParameters.COMPLETED, true)

        properties.put("event_parameters", eventParams)

        every { mockTracker.logEvent(any(), any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.START_TRIAL), properties)
        verify { mockTracker.logEvent(Commands.START_TRIAL, eventParams) }
        confirmVerified(mockTracker)
    }

    @Test
    fun eventWithCustomParameters() {
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

        every { mockTracker.customEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf(Commands.START_TRIAL), parameters)
        verify {
            mockTracker.customEvent(Commands.START_TRIAL, eventParams.toString())
        }
        confirmVerified(mockTracker)
    }
}
