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

/**
 * Max number of minutes break between lightning talks
 */
private const val LIGHTNING_BREAKS = 11L

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

    val mergedLighting = mergeLightningTalks(lightning)

    sessions.addAll(mergedLighting)
    val sorted = sessions.sortedWith { o1, o2 ->
        var ret = o1.time.compareTo(o2.time)
        if (ret == 0) {
            ret = o1.room.compareTo(o2.room)
        }

        ret
    }

    return sorted
}

fun mergeLightningTalks(talks: MutableList<ConferenceTalk>): List<ConferenceSession> {
    val roomTalks: MutableMap<ConferenceRoom, MutableList<ConferenceTalk>> = mutableMapOf()

    for (talk in talks) {
        val pair = talk.room to talk
        val orDefault = roomTalks.getOrDefault(pair.first, mutableListOf())
        orDefault.add(pair.second)
        roomTalks.put(pair.first, orDefault)
    }

    roomTalks.forEach {
        it.value.sortWith { o1, o2 -> o1.startTime.compareTo(o2.startTime) }
    }

    val sessions = mutableListOf<ConferenceSession>()
    var prev: ConferenceTalk? = null
    lateinit var current: MutableList<ConferenceTalk>
    for (roomTalk in roomTalks) {
        for (talk in roomTalk.value) {
            if (prev == null) {
                current = mutableListOf(talk)
                prev = talk
                continue
            }
            // For 10 min lightning talks, there is a 5 min breaks between each session.
            // For 20 min lightning talks, there is a 10 min break.
            if (prev.endTime.plusMinutes(LIGHTNING_BREAKS).isAfter(talk.startTime)) {
                current.add(talk)
                prev = talk
            } else {
                sessions.add(ConferenceSession(time = current.first().startTime, talks = current))
                current = mutableListOf(talk)
                prev = talk
            }
        }
        sessions.add(ConferenceSession(time = current.first().startTime, talks = current))
        current = mutableListOf()
        prev = null
    }

    return sessions
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
            room = ConferenceRoom.create(room)
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
        avatarUrl = it.pictureUrl,
        twitter = it.twitter
    )
}

