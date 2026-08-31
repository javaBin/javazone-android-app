package no.javazone.scheduler.model

import android.util.Log
import no.javazone.scheduler.dto.*
import no.javazone.scheduler.utils.FIRST_CONFERENCE_DAY
import no.javazone.scheduler.utils.JAVAZONE_DATE_PATTERN
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.WORKSHOP_DAY
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DEFAULT_WORKSHOP_START_TIME =
    OffsetDateTime.of(WORKSHOP_DAY, LocalTime.of(9, 0), ZoneOffset.UTC)
private val DEFAULT_FIRST_START_TIME =
    OffsetDateTime.of(FIRST_CONFERENCE_DAY, LocalTime.NOON, ZoneOffset.UTC)
private val DEFAULT_WORKSHOP_END_TIME = OffsetDateTime.of(
    WORKSHOP_DAY,
    LocalTime.of(16, 0),
    ZoneOffset.UTC
)
private val DEFAULT_FIRST_END_TIME = OffsetDateTime.of(
    FIRST_CONFERENCE_DAY,
    LocalTime.of(16, 0),
    ZoneOffset.UTC
)

fun ConferenceDto.toModel(): Conference =
    Conference(
        name = this.conferenceName,
        days = this.conferenceDates.map { it.toModel(false) } + this.workshopDate.toModel(true),
        conferenceUrl = this.conferenceUrl
    )

fun SessionsDto.toModel(): List<ConferenceSession> = convertDtoSessions(sessions)

fun dtoToPartner(): (PartnerDto) -> Partner = {
    Partner(
        name = it.name,
        homepageUrl = it.homepageUrl,
        logoUrl = it.logoUrl
    )
}

private fun String.toModel(isWorkshop: Boolean): ConferenceDate {
    val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))
    return ConferenceDate(
        date = date,
        label = if (isWorkshop) "workshop" else date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    )
}

private fun convertDtoSessions(sessionsDto: List<SessionDto>): List<ConferenceSession> {
    val sessions = mutableListOf<ConferenceSession>()
    val lightning = mutableListOf<ConferenceTalk>()

    for (dto in sessionsDto) {
        val talk: ConferenceTalk? = dto.toModel()
        if (talk?.format == ConferenceFormat.LIGHTNING_TALK) {
            lightning.add(talk)
        } else if (talk != null) {
            sessions.add(ConferenceSession(talk))
        }
    }

    // The real time-slot grid per day, defined by the non-lightning sessions:
    // presentations/workshops start on the conference grid (e.g. 09:00, 10:20, 11:40, …).
    val slotStartsByDay: Map<LocalDate, List<OffsetDateTime>> = sessions
        .map { it.time }
        .groupBy { it.toLocalDate() }
        .mapValues { (_, times) -> times.distinct().sorted() }

    sessions.addAll(groupLightningTalksBySlot(lightning, slotStartsByDay))

    val sorted = sessions.sortedWith { o1, o2 ->
        var ret = o1.time.compareTo(o2.time)
        if (ret == 0) {
            ret = o1.room.compareTo(o2.room)
        }

        ret
    }

    return sorted
}

/**
 * Groups lightning talks into the conference's real time slots rather than by ad-hoc
 * gaps: every lightning talk is bucketed into the slot it belongs to (see [slotStartFor]),
 * and each bucket becomes one [ConferenceSession] timestamped with the slot's start. This
 * keeps a slot's lightning talks together under a single, correct header even though the
 * talks themselves start at staggered times within the slot.
 */
fun groupLightningTalksBySlot(
    talks: List<ConferenceTalk>,
    slotStartsByDay: Map<LocalDate, List<OffsetDateTime>>
): List<ConferenceSession> =
    talks
        .groupBy { talk -> slotStartFor(talk, slotStartsByDay) }
        .map { (slotStart, slotTalks) ->
            ConferenceSession(
                time = slotStart,
                talks = slotTalks.sortedBy { it.startTime }
            )
        }

/**
 * The grid slot a lightning talk belongs to: the latest non-lightning slot start on the
 * same day that is at or before the talk's start. Falls back to the talk's own start time
 * when it precedes every slot, or when the day has no grid (e.g. a lightning-only day).
 */
private fun slotStartFor(
    talk: ConferenceTalk,
    slotStartsByDay: Map<LocalDate, List<OffsetDateTime>>
): OffsetDateTime {
    val daySlots = slotStartsByDay[talk.startTime.toLocalDate()].orEmpty()
    return daySlots.lastOrNull { !it.isAfter(talk.startTime) } ?: talk.startTime
}

private fun SessionDto.toModel(): ConferenceTalk? {
    return try {
        ConferenceTalk(
            id = sessionId,
            title = title,
            startTime = if (startTimeZulu != null) OffsetDateTime.parse(startTimeZulu) else getDefaultTime(
                format,
                DEFAULT_WORKSHOP_START_TIME,
                DEFAULT_FIRST_START_TIME
            ),
            endTime = if (endTimeZulu != null) OffsetDateTime.parse(endTimeZulu) else getDefaultTime(
                format,
                DEFAULT_WORKSHOP_END_TIME,
                DEFAULT_FIRST_END_TIME
            ),
            length = length,
            intendedAudience = intendedAudience,
            language = language,
            summary = abstract,
            speakers = speakers.map(toConferenceSpeaker()).toSet(),
            video = video,
            format = format.toConferenceFormat(),
            room = ConferenceRoom.create(room),
            registrationLink = registerLoc,
            suggestedKeywords = suggestedKeywords
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()
        )
    } catch (ex: Exception) {
        Log.e(LOG_TAG, "Unknown format: $format")
        null
    }
}

private fun getDefaultTime(
    format: String,
    workshopTime: OffsetDateTime,
    conferenceTime: OffsetDateTime
): OffsetDateTime {
    val conference = format.toConferenceFormat()
    return if (conference == ConferenceFormat.WORKSHOP) {
        workshopTime
    } else {
        conferenceTime
    }
}

private fun toConferenceSpeaker(): (SpeakerDto) -> ConferenceSpeaker = {
    ConferenceSpeaker(
        name = it.name,
        bio = it.bio,
        twitter = it.twitter,
        bluesky = it.bluesky,
        linkedin = it.linkedin
    )
}

