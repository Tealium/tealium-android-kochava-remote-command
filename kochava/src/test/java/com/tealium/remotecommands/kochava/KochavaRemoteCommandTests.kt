package com.tealium.remotecommands.kochava

import android.app.Application
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KochavaRemoteCommandTests {

    @MockK
    lateinit var mockApplication: Application

    @MockK
    lateinit var mockKochavaInstance: KochavaCommand

    lateinit var kochavaRemoteCommand: KochavaRemoteCommand

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        kochavaRemoteCommand = KochavaRemoteCommand(mockApplication, "test123")
        kochavaRemoteCommand.kochavaInstance = mockKochavaInstance
    }

    @Test
    fun testSplitCommands() {
        val json = JSONObject()
        json.put("command_name", "Purchase,CompleteTutorial,CheckoutStart")
        val commands = kochavaRemoteCommand.splitCommands(json)

        assertEquals(3, commands.count())
        assertEquals("purchase", commands[0])
        assertEquals("completetutorial", commands[1])
        assertEquals("checkoutstart", commands[2])
    }

    @Test
    fun testInitialize() {
        val config = JSONObject()
        config.put("app_gui", "testId")
        config.put("logLevel", "dev")

        val payload = JSONObject()
        payload.put(EventKey.CONFIGURATION_PARAMS, config)
        payload.put("command_name", Commands.INITIALIZE)

        every { mockKochavaInstance.initialize(any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("initialize"), payload)

        verify { mockKochavaInstance.initialize(config) }

        confirmVerified(mockKochavaInstance)
    }

    @Test
    fun testSetIdentityLink() {
        val idLinks = JSONObject()
        idLinks.put("userName", "tester")
        idLinks.put("userId", "123test")

        val payload = JSONObject()
        payload.put(EventKey.IDENTITY_LINKS, idLinks)
        payload.put("command_name", Commands.SET_IDENTITY_LINK)

        every { mockKochavaInstance.setIdentityLink(any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("setidentitylink"), payload)

        verify { mockKochavaInstance.setIdentityLink(idLinks) }

        confirmVerified(mockKochavaInstance)
    }

    @Test
    fun testSleepTracker() {

        val payload = JSONObject()
        payload.put(EventKey.SLEEP, true)
        payload.put("command_name", Commands.SLEEP_TRACKER)

        every { mockKochavaInstance.setSleep(any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("sleeptracker"), payload)

        verify { mockKochavaInstance.setSleep(true) }

        confirmVerified(mockKochavaInstance)
    }

    @Test
    fun testCustomEventNoParameters() {
        val payload = JSONObject()
        payload.put(EventKey.CUSTOM_EVENT_NAME, "test_event")
        payload.put("command_name", Commands.CUSTOM_EVENT)

        every { mockKochavaInstance.sendCustomEvent(any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("custom"), payload)

        verify { mockKochavaInstance.sendCustomEvent("test_event") }

        confirmVerified(mockKochavaInstance)
    }

    @Test
    fun testCustomEventWithCustomParameters() {
        val customParams = JSONObject()
        customParams.put("key1", "value1")
        customParams.put("key2", "value2")
        customParams.put("key3", "value3")

        val payload = JSONObject()
        payload.put(EventKey.CUSTOM, customParams)
        payload.put(EventKey.CUSTOM_EVENT_NAME, "test_event")
        payload.put("command_name", Commands.CUSTOM_EVENT)

        every { mockKochavaInstance.sendCustomEvent(any(), any()) } just Runs

        kochavaRemoteCommand.parseCommands(arrayOf("custom"), payload)

        verify { mockKochavaInstance.sendCustomEvent("test_event", customParams) }

        confirmVerified(mockKochavaInstance)
    }
}