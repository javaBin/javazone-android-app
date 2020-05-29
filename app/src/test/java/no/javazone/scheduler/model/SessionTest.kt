package no.javazone.scheduler.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class SessionTest {
    @Test
    fun test() {
        assertThrows<AssertionError> { Session(Room.create("room 1"), emptyList()) }
    }

    @Test
    fun test1() {
        val talk1 = Lightning(
            sessionId = UUID.randomUUID().toString(),
            title = "test1",
            startTime = OffsetDateTime.of(LocalDate.now(), LocalTime.NOON, ZoneOffset.UTC),
            endTime = OffsetDateTime.of(
                LocalDate.now(),
                LocalTime.NOON.plusMinutes(20L),
                ZoneOffset.UTC
            ),
            length = 20,
            intendedAudience = "",
            language = "en",
            video = "",
            abstract = "",
            speakers = setOf(Speaker(name = "test", bio = "test"))
        )

        val result = Session(room = Room.create("room 1"), talks = listOf(talk1))

        assertThat(result.startTime).isEqualTo(talk1.startTime)
    }
}