package no.javazone.scheduler.ui.schedules

import com.google.common.truth.Truth.assertThat
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceRoom
import no.javazone.scheduler.model.ConferenceSpeaker
import no.javazone.scheduler.model.ConferenceTalk
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Tests for [currentSlotItemIndex] — the logic behind the "Jump to now" button.
 *
 * The function takes `now` as a parameter, so the current-time behaviour can be verified
 * for arbitrary points in time without touching the system clock (useful because the real
 * conference is scheduled well in the future).
 *
 * Flat [androidx.compose.foundation.lazy.LazyColumn] index layout for the fixture below
 * (one sticky header item precedes each day's talks):
 *
 * ```
 * 0  header (day 1)
 * 1  T1  09:00–09:45
 * 2  T2  10:00–10:45
 * 3  T3  11:00–11:45
 * 4  header (day 2)
 * 5  T4  09:00–09:45
 * 6  T5  10:00–10:45
 * ```
 */
class MyScheduleRouteTest {

    private val zone = ZoneOffset.ofHours(2)
    private val day1: LocalDate = LocalDate.of(2026, 9, 2)
    private val day2: LocalDate = LocalDate.of(2026, 9, 3)

    // Insertion order matters: currentSlotItemIndex walks the map (and each talk list) in
    // order, mirroring how the LazyColumn renders them. selectMySchedule produces exactly
    // this shape (sorted by slot time, grouped by date into a LinkedHashMap).
    private val schedule: Map<LocalDate, List<ConferenceTalk>> = linkedMapOf(
        day1 to listOf(
            talk("t1", day1, 9, 0, 9, 45),
            talk("t2", day1, 10, 0, 10, 45),
            talk("t3", day1, 11, 0, 11, 45),
        ),
        day2 to listOf(
            talk("t4", day2, 9, 0, 9, 45),
            talk("t5", day2, 10, 0, 10, 45),
        ),
    )

    @Test
    fun `before any talk starts returns the first talk`() {
        val now = at(day1, 8, 0)
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(1)
    }

    @Test
    fun `during a talk returns that ongoing talk`() {
        val now = at(day1, 10, 15) // T1 has ended, T2 is running
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(2)
    }

    @Test
    fun `just before a talk's end time it is still the target`() {
        val now = at(day1, 9, 44) // T1 ends 09:45, so it is still ongoing
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(1)
    }

    @Test
    fun `exactly at a talk's end time it is treated as ended`() {
        val now = at(day1, 9, 45) // endTime is exclusive -> move on to T2
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(2)
    }

    @Test
    fun `in the gap after a day's last talk returns the next day's first talk`() {
        val now = at(day1, 12, 0) // all day-1 talks ended; crosses the day-2 header
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(5)
    }

    @Test
    fun `during the very last talk returns its index`() {
        val now = at(day2, 10, 15)
        assertThat(currentSlotItemIndex(schedule, now)).isEqualTo(6)
    }

    @Test
    fun `after every talk has ended returns null`() {
        val now = at(day2, 12, 0)
        assertThat(currentSlotItemIndex(schedule, now)).isNull()
    }

    @Test
    fun `an empty schedule returns null`() {
        assertThat(currentSlotItemIndex(emptyMap(), at(day1, 10, 0))).isNull()
    }

    private fun at(date: LocalDate, hour: Int, minute: Int): OffsetDateTime =
        OffsetDateTime.of(date, LocalTime.of(hour, minute), zone)

    private fun talk(
        id: String,
        date: LocalDate,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ): ConferenceTalk {
        val start = OffsetDateTime.of(date, LocalTime.of(startHour, startMinute), zone)
        val end = OffsetDateTime.of(date, LocalTime.of(endHour, endMinute), zone)
        return ConferenceTalk(
            id = id,
            title = id,
            length = java.time.Duration.between(start, end).toMinutes().toInt(),
            intendedAudience = "",
            language = "en",
            video = null,
            summary = "",
            format = ConferenceFormat.PRESENTATION,
            room = ConferenceRoom.create("Room 1"),
            startTime = start,
            endTime = end,
            speakers = setOf(ConferenceSpeaker(name = "Speaker", bio = "")),
            registrationLink = null,
        )
    }
}
