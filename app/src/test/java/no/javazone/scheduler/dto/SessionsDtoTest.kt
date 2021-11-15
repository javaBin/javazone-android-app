package no.javazone.scheduler.dto

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class SessionsDtoTest {
    private val path = Paths.get("src/test/res")

    @Test
    fun `parsing json works`() {
        val jsonStringBuffer = String(Files.readAllBytes(path.resolve("sessions.json")))

        val result = Json.decodeFromString(SessionsDto.serializer(), jsonStringBuffer)

        assertThat(result).isNotNull()
    }
}