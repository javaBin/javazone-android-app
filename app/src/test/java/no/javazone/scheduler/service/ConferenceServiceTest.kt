package no.javazone.scheduler.service

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ConferenceServiceTest {
    private lateinit var service: ConferenceService

    @Before
    internal fun setUp() {
        service = ConferenceService.create()
    }

    @After
    internal fun tearDown() {
    }

    @Test
    fun test() = runBlocking {
            val conference = service.getConference()
            assertThat(conference).isNotNull()
        }
}