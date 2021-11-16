package no.javazone.scheduler.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import no.javazone.scheduler.utils.JAVAZONE_BASE_URL
import org.junit.Before
import org.junit.Test

class NetworkClientTest {
    private lateinit var client: NetworkClient

    @Before
    fun setUp() {
        client = NetworkClient.create(JAVAZONE_BASE_URL)
    }

    @Test
    fun test() = runBlocking {
        val conference = client.getConference()
        assertThat(conference.conferenceDates).isNotEmpty()
    }
}