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
    fun customEvent() {
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
    fun standardEvent() {
        val properties = JSONObject()
        val eventParams = JSONObject()
        val infoDictionary = JSONObject()

        eventParams.put(EventParameters.USER_ID, "abc")
        eventParams.put(EventParameters.NAME, "name")
        eventParams.put(EventParameters.CURRENCY, "USD")
        eventParams.put(EventParameters.COMPLETED, true)

        infoDictionary.put("key", "value")
        eventParams.put("info_dictionary", infoDictionary)

        properties.put("event_parameters", eventParams)

        every {
            mockTracker.standardEvent(any(), any()) } just Runs
        kochavaRemoteCommand.parseCommands(arrayOf(Commands.START_TRIAL), properties)
        verify {
            mockTracker.standardEvent(Commands.START_TRIAL, eventParams.toString())
        }
        confirmVerified(mockTracker)
    }
}
