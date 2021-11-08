package no.javazone.scheduler.api

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class AssetConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    @Before
    fun setUp() {
        api = AssetConferenceSession.getInstance(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun test() {
        val result = api.fetch()
        assertThat(result).isNotEmpty()
    }
}