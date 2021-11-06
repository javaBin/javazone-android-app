package no.javazone.scheduler.api

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssetConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    @Before
    fun setUp() {
        api = AssetConferenceSession.getInstance(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun test() {
        val result = api.fetch()
        Assert.assertNotNull(result)
    }
}