package no.javazone.scheduler.dto

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class SessionsDtoTest {
    private val path = Paths.get("src/test/res")

    @Test
    fun test() {
        val jsonStringBuffer = String(Files.readAllBytes(path.resolve("sessions.json")))

        val json = Json(JsonConfiguration.Stable)
        val result = json.parse(SessionsDto.serializer(), jsonStringBuffer)

        assertThat(result).isNotNull()
    }
}