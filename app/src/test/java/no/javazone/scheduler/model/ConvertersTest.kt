package no.javazone.scheduler.model

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import no.javazone.scheduler.dto.SessionsDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class ConvertersTest {
    private val path = Paths.get("src/test/res")
    private lateinit var dto: SessionsDto

    @BeforeEach
    internal fun setUp() {
        val jsonStringBuffer = String(Files.readAllBytes(path.resolve("sessions.json")))

        val json = Json(JsonConfiguration.Stable)
        dto = json.parse(SessionsDto.serializer(), jsonStringBuffer)
    }

    @Test
    fun `conversion from dto to domain doesn't lose information`() {
        val workshops = 11
        val presentations = 84
        val lightnings = 44

        val result = dto.toModel()

        assertThat(result).isNotEmpty()
        assertThat(result.filter { it.talks.size == 1 }.filter { it.talks.first() is Workshop }).hasSize(workshops)
        assertThat(result.filter { it.talks.size == 1 }.filter { it.talks.first() is Presentation }).hasSize(presentations)
        assertThat(result.filter { it.talks.first() is Lightning }.flatMap { it.talks }).hasSize(44)

        val test = result.filter { it.talks.size == 1 }.filter { it.talks.first() is Lightning }
        assertThat(test).isNotEmpty()
    }
}