package no.javazone.scheduler.model

import android.util.Log
//import androidx.room.TypeConverter
import no.javazone.scheduler.dto.SessionDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.dto.SpeakerDto
import no.javazone.scheduler.utils.JAVAZONE_DATE_PATTERN
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "Converters"

class Converters {
//    @TypeConverter
    fun localDateToString(date: LocalDate): Long =
        date.format(DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN)).toLong()

//    @TypeConverter
    fun stringToLocalDate(value: Long): LocalDate =
        LocalDate.parse(value.toString(), DateTimeFormatter.ofPattern(JAVAZONE_DATE_PATTERN))

//    @TypeConverter
    fun offsetDateTimeToTimestamp(date: OffsetDateTime): Long =
        date.toEpochSecond()

//    @TypeConverter
    fun timeStampToOffsetDateTime(value: Long): OffsetDateTime =
        OffsetDateTime.from(Instant.ofEpochSecond(value))
}

fun SessionsDto.toModel(): List<ConferenceSession> = convertDtoSessions(sessions)

private fun convertDtoSessions(sessionsDto: List<SessionDto>): List<ConferenceSession> {
    val sessions = mutableListOf<ConferenceSession>()
    val lightning = mutableListOf<ConferenceSession>()

    for (dto in sessionsDto) {
        val talk: Talk? = dto.toModel()
        if (talk?.format == ConferenceFormat.LIGHTNING_TALK) {
            lightning.add(ConferenceSession(room = ConferenceRoom.create(dto.room), talks = listOf(talk)))
        } else if (talk != null) {
            sessions.add(ConferenceSession(room = ConferenceRoom.create(dto.room), talks = listOf(talk)))
        }
    }

    val mergedLighting = mergeLightningTalks(lightning)

    sessions.addAll(mergedLighting)
    sessions.sortWith(Comparator { o1, o2 ->
        var ret = o1.startTime.compareTo(o2.startTime)
        if (ret == 0) {
            ret = o1.room.compareTo(o2.room)
        }

        ret
    })

    return sessions
}

fun mergeLightningTalks(talks: MutableList<ConferenceSession>): List<ConferenceSession> {
    val roomTalks: MutableMap<ConferenceRoom, MutableList<Talk>> = mutableMapOf()

    for (talk in talks) {
        val pair = talk.room to talk.talks.first()
        val orDefault = roomTalks.getOrDefault(pair.first, mutableListOf())
        orDefault.add(pair.second)
        roomTalks.put(pair.first, orDefault)
    }

    roomTalks.forEach {
        it.value.sortWith(Comparator { o1, o2 -> o1.startTime.compareTo(o2.startTime) })
    }

    val sessions = mutableListOf<ConferenceSession>()
    var prev: Talk? = null
    lateinit var current: MutableList<Talk>
    for (roomTalk in roomTalks) {
        for (talk in roomTalk.value) {
            if (prev == null) {
                current = mutableListOf(talk)
                prev = talk
                continue
            }
            if (prev.endTime.isEqual(talk.startTime)) {
                current.add(talk)
                prev = talk
            } else {
                sessions.add(ConferenceSession(room = roomTalk.key, talks = current))
                current = mutableListOf(talk)
                prev = talk
            }
        }
        sessions.add(ConferenceSession(room = roomTalk.key, talks = current))
        current = mutableListOf()
        prev = null
    }

    return sessions
}

private fun SessionDto.toModel(): Talk? {
    return try {
        Talk(
            id = sessionId,
            title = title,
            startTime = OffsetDateTime.parse(startTimeZulu),
            endTime = OffsetDateTime.parse(endTimeZulu),
            length = length,
            intendedAudience = intendedAudience,
            language = language,
            abstract = abstract,
            speakers = speakers.map { it.toModel() }.toSet(),
            video = video,
            format = format.toConferenceFormat()
        )
    } catch (ex: Exception) {
        Log.e(TAG, "Unknown format: $format")
        null
    }
}

private fun SpeakerDto.toModel(): Speaker =
    Speaker(
        name = name,
        bio = bio,
        avatar = pictureUrl,
        twitter = twitter
    )

