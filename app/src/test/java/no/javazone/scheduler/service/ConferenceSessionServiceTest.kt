package no.javazone.scheduler.service

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import no.javazone.scheduler.dto.ConferenceDto
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class ConferenceSessionServiceTest {
    companion object {
        private val PATH = Paths.get("src/main/assets")
        private lateinit var conference: ConferenceDto

        @JvmStatic
        @BeforeClass
        internal fun initTest() {
            val jsonStringBuffer = String(Files.readAllBytes(PATH.resolve("conference.json")))

            conference = Json.decodeFromString(ConferenceDto.serializer(), jsonStringBuffer)
        }

    }

    private lateinit var service: SessionService

    @Before
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