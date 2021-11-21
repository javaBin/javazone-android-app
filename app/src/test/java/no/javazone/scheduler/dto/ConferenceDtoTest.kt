package no.javazone.scheduler.dto

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class ConferenceDtoTest {
    private val path = Paths.get("src/main/assets")

    @Test
    fun `parsing json works`() {
        val expected = ConferenceDto(
            conferenceName = "JavaZone 2019",
            workshopDate = "10.09.2019",
            conferenceDates = listOf("11.09.2019", "12.09.2019"),
            conferenceUrl = "https://sleepingpill.javazone.no/public/allSessions/javazone_2019")

        val jsonStringBuffer = String(Files.readAllBytes(path.resolve("conference2019.json")))

        val result = Json.decodeFromString(ConferenceDto.serializer(), jsonStringBuffer)

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(expected)
    }
}