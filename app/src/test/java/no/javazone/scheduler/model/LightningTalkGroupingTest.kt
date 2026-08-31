package no.javazone.scheduler.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Verifies that lightning talks are bucketed into the conference's real time-slot grid
 * (the start times of the non-lightning sessions) instead of ad-hoc gap merging.
 *
 * Mirrors the JavaZone 2026 Wednesday schedule, whose slots are 09:00, 10:20, 11:40,
 * 13:00, 14:20, 15:40, 17:00 — lightning talks start at staggered times within each slot.
 */
class LightningTalkGroupingTest {

    private val zone = ZoneOffset.ofHours(2)
    private val day: LocalDate = LocalDate.of(2026, 9, 2)

    private val grid = listOf("09:00", "10:20", "11:40", "13:00", "14:20", "15:40", "17:00")
        .map { at(it) }
    private val slotStartsByDay = mapOf(day to grid)

    @Test
    fun `lightning talks are grouped into the grid slot they fall in`() {
        val talks = listOf(
            lt("09:00", "09:10"), lt("09:15", "09:35"), lt("10:05", "10:25"), // -> 09:00
            lt("10:30", "10:40"), lt("11:00", "11:45"),                       // -> 10:20
            lt("11:50", "12:10"), lt("12:15", "12:25"), lt("12:55", "13:05"), // -> 11:40
            lt("13:25", "13:35"),                                             // -> 13:00
            lt("17:05", "17:15"), lt("18:00", "18:10"),                       // -> 17:00
        )

        val sessions = groupLightningTalksBySlot(talks, slotStartsByDay)

        val bySlot = sessions.associate { it.time.toLocalTime().toString() to it.talks.size }
        assertThat(bySlot).containsExactlyEntriesIn(
            mapOf("09:00" to 3, "10:20" to 2, "11:40" to 3, "13:00" to 1, "17:00" to 2)
        )
    }

    @Test
    fun `talks within a slot keep their own staggered start times, sorted`() {
        val talks = listOf(lt("11:50", "12:10"), lt("12:15", "12:25"), lt("11:40", "11:45"))

        val session = groupLightningTalksBySlot(talks, slotStartsByDay).single()

        assertThat(session.time).isEqualTo(at("11:40"))
        assertThat(session.talks.map { it.startTime.toLocalTime().toString() })
            .containsExactly("11:40", "11:50", "12:15").inOrder()
        // Every talk in the merged block is stamped with the slot start for the header,
        // while its own startTime is preserved on the talk itself.
        assertThat(session.talks.map { it.slotTime }.distinct()).containsExactly(at("11:40"))
    }

    @Test
    fun `a talk before the first slot falls back to its own start time`() {
        val talks = listOf(lt("08:30", "08:40"))

        val session = groupLightningTalksBySlot(talks, slotStartsByDay).single()

        assertThat(session.time).isEqualTo(at("08:30"))
    }

    @Test
    fun `with no grid for the day each talk falls back to its own start time`() {
        val talks = listOf(lt("09:00", "09:10"), lt("09:15", "09:25"))

        val sessions = groupLightningTalksBySlot(talks, emptyMap())

        assertThat(sessions.map { it.time.toLocalTime().toString() })
            .containsExactly("09:00", "09:15")
    }

    private fun at(hhmm: String): OffsetDateTime =
        OffsetDateTime.of(day, LocalTime.parse(hhmm), zone)

    private fun lt(start: String, end: String): ConferenceTalk = ConferenceTalk(
        id = "$start-$end",
        title = "Lightning $start",
        length = 10,
        intendedAudience = "",
        language = "en",
        video = null,
        summary = "",
        format = ConferenceFormat.LIGHTNING_TALK,
        room = ConferenceRoom.create("Room VI"),
        startTime = at(start),
        endTime = at(end),
        speakers = setOf(ConferenceSpeaker(name = "Speaker", bio = "")),
        registrationLink = null,
    )
}
