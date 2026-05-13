package no.javazone.scheduler.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import no.javazone.scheduler.utility.TestDispatchersProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AssetConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    private lateinit var testDispatchers: TestDispatchersProvider

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scheduler = TestCoroutineScheduler()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDispatchers = TestDispatchersProvider(scheduler)
        api = AssetConferenceSession.getInstance(context, testDispatchers)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conferencesessions should work`() = runTest(scheduler) {
        val result = api.fetchSessions("")
        assertThat(result).isNotEmpty()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conference should work`() = runTest(scheduler) {
        val result = api.fetchConference()
        assertThat(result.days).isNotEmpty()
    }
}