package no.javazone.scheduler.service

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import no.javazone.scheduler.dto.ConferenceDto
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Files
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConferenceSessionServiceTest {
    companion object {
        private val PATH = Paths.get("src/main/assets")
    }

    private lateinit var service: SessionService
    private lateinit var conference: ConferenceDto

    @BeforeAll
    internal fun initTest() {
        val jsonStringBuffer = String(Files.readAllBytes(PATH.resolve("conference.json")))

        conference = Json.decodeFromString(ConferenceDto.serializer(), jsonStringBuffer)
    }

    @BeforeEach
    internal fun setUp() {
        service = SessionService.create(conference.conferenceUri.scheme + "://" + conference.conferenceUri.host)
    }

    @Test
    fun test() = runBlocking {
        println(conference.conferenceUrl)
        val result = service.getSessions(conference.conferenceUrlPath)
        assertThat(result).isNotNull()
    }
}