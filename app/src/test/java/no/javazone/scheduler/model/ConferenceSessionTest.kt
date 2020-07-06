package no.javazone.scheduler.model

import com.google.common.truth.Truth.assertThat
import no.javazone.scheduler.BuildConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class ConferenceSessionTest {
    @Test
    fun `no session should be created for a room without a talk`() {
        if (BuildConfig.DEBUG) {
            assertThrows<IllegalStateException> {
                ConferenceSession(
                    ConferenceRoom.create("room 1"),
                    emptyList()
                )
            }
        }
    }

    @Test
    fun `session slot is based on talks start and endtime`() {
        val startTime = OffsetDateTime.of(LocalDate.now(), LocalTime.NOON, ZoneOffset.UTC)
        val talk1 = Talk(
            id = UUID.randomUUID().toString(),
            title = "test1",
            startTime = startTime,
            endTime = startTime.plusMinutes(20L),
            length = 20,
            intendedAudience = "",
            language = "en",
            video = "",
            abstract = "",
            speakers = setOf(Speaker(name = "test", bio = "test")),
            format = ConferenceFormat.LIGHTNING_TALK
        )
        val talk2 = Talk(
            id = UUID.randomUUID().toString(),
            title = "test1",
            startTime = talk1.endTime,
            endTime = talk1.endTime.plusMinutes(20L),
            length = 20,
            intendedAudience = "",
            language = "en",
            video = "",
            abstract = "",
            speakers = setOf(Speaker(name = "test", bio = "test")),
            format = ConferenceFormat.LIGHTNING_TALK
        )

        val result = ConferenceSession(room = ConferenceRoom.create("room 1"), talks = listOf(talk1, talk2))

        assertThat(result.startTime).isEqualTo(talk1.startTime)
        assertThat(result.endTime).isEqualTo(talk2.endTime)
    }
}