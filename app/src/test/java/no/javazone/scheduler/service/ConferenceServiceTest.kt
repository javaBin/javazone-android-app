package no.javazone.scheduler.service

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConferenceServiceTest {
    private lateinit var service: ConferenceService

    @BeforeEach
    internal fun setUp() {
        service = ConferenceService.create()
    }

    @AfterEach
    internal fun tearDown() {
    }

    @Test
    fun test() = runBlocking {
            val conference = service.getConference()
            assertThat(conference).isNotNull()
        }
}