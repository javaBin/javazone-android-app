package no.javazone.scheduler.model

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class ConferenceSessionTest {
    @Test
    fun `no session should be created for a room without a talk`() {
        try {
            ConferenceSession(
                time = OffsetDateTime.now(),
                talks = emptyList()
            )
            fail("no talks should throw exception")
        } catch (ignore: IllegalStateException) {
        }
    }

    @Test
    fun `session slot is based on talks start and endtime`() {
        val startTime = OffsetDateTime.of(LocalDate.now(), LocalTime.NOON, ZoneOffset.UTC)
        val talk1 = ConferenceTalk(
            talkId = UUID.randomUUID().toString(),
            title = "test1",
            startTime = startTime,
            endTime = startTime.plusMinutes(20L),
            length = 20,
            intendedAudience = "",
            language = "en",
            video = "",
            summary = "",
            speakers = setOf(ConferenceSpeaker(name = "test", bio = "test")),
            format = ConferenceFormat.LIGHTNING_TALK,
            room = ConferenceRoom.DEFAULT
        )
        val talk2 = ConferenceTalk(
            talkId = UUID.randomUUID().toString(),
            title = "test1",
            startTime = talk1.endTime,
            endTime = talk1.endTime.plusMinutes(20L),
            length = 20,
            intendedAudience = "",
            language = "en",
            video = "",
            summary = "",
            speakers = setOf(ConferenceSpeaker(name = "test", bio = "test")),
            format = ConferenceFormat.LIGHTNING_TALK,
            room = ConferenceRoom.DEFAULT
        )

        val result =
            ConferenceSession(talk1.startTime, talks = listOf(talk1, talk2))

        assertThat(result.time).isEqualTo(talk1.startTime)
    }
}