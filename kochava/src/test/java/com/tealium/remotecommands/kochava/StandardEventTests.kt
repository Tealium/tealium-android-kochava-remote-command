package com.tealium.remotecommands.kochava

import android.app.Application
import com.kochava.base.Tracker
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StandardEventTests {
    @MockK
    lateinit var mockApplication: Application

    @MockK
    lateinit var mockKochavaInstance: KochavaCommand

    lateinit var kochavaRemoteCommand: KochavaRemoteCommand

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        kochavaRemoteCommand = KochavaRemoteCommand(
            mockApplication,
            "testKey"
        )

        kochavaRemoteCommand.kochavaInstance = mockKochavaInstance
    }

    @Test
    fun testStandardEventSuccess() {
        val isStandardEvent = kochavaRemoteCommand.standardEvent("registrationcomplete")
        assertNotNull(isStandardEvent)
    }

    @Test
    fun testStandardEventFailure() {
        val isStandardEvent = kochavaRemoteCommand.standardEvent("testEvent")
        assertNull(isStandardEvent)
    }

    @Test
    fun testStandardEventWithData() {
        val payload = JSONObject()
        val eventParams = JSONObject()
        eventParams.put(ParameterKey.AD_CAMPAIGN_NAME, "test_ad")
        eventParams.put(ParameterKey.AD_CAMPAIGN_ID, "ad123")

        payload.put(EventKey.EVENT_PARAMS, eventParams)

        kochavaRemoteCommand.parseCommands(arrayOf("adview"), payload)

        every {
            mockKochavaInstance.sendEvent(any(), any())
        } just Runs

        verify {
            mockKochavaInstance.sendEvent(
                Tracker.EVENT_TYPE_AD_VIEW,
                eventParams
            )
        }

        confirmVerified(mockKochavaInstance)
    }

    @Test
    fun testStandardEventWithoutOptionalData() {
        val payload = JSONObject()
        payload.put("event", JSONObject())
        kochavaRemoteCommand.parseCommands(arrayOf("rating"), payload)

        every {
            mockKochavaInstance.sendEvent(any(), any())
        } just runs

        verify {
            mockKochavaInstance.sendEvent(Tracker.EVENT_TYPE_RATING)
        }

        confirmVerified(mockKochavaInstance)
    }

}