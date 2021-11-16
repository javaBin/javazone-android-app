package no.javazone.scheduler.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AssetConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        api = AssetConferenceSession.getInstance(context, TestCoroutineDispatcher())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conferencesessions should work`() = runBlockingTest {
        val result = api.fetchConferenceSessions()
        assertThat(result).isNotEmpty()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conference should work`() = runBlockingTest {
        val result = api.fetchConference()
        assertThat(result.days).isNotEmpty()
    }
}