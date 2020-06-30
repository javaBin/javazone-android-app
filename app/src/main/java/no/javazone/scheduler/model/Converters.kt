package no.javazone.scheduler.model

import android.util.Log
import no.javazone.scheduler.dto.SessionDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.dto.SpeakerDto
import java.time.OffsetDateTime

private const val TAG = "Converters"

fun SessionsDto.toModel(): List<Session> = convertDtoSessions(sessions)

private fun convertDtoSessions(sessionsDto: List<SessionDto>): List<Session> {
    val sessions = mutableListOf<Session>()
    val lightning = mutableListOf<Session>()

    for (dto in sessionsDto) {
        val talk: Talk? = dto.toModel()
        if (talk is Lightning) {
            lightning.add(Session(room = Room.create(dto.room), talks = listOf(talk)))
        } else if (talk != null) {
            sessions.add(Session(room = Room.create(dto.room), talks = listOf(talk)))
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

fun mergeLightningTalks(talks: MutableList<Session>): List<Session> {
    val roomTalks: MutableMap<Room, MutableList<Talk>> = mutableMapOf()

    for (talk in talks) {
        val pair = talk.room to talk.talks.first()
        val orDefault = roomTalks.getOrDefault(pair.first, mutableListOf())
        orDefault.add(pair.second)
        roomTalks.put(pair.first, orDefault)
    }

    roomTalks.forEach {
        it.value.sortWith(Comparator { o1, o2 -> o1.startTime.compareTo(o2.startTime) })
    }

    val sessions = mutableListOf<Session>()
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
                sessions.add(Session(room = roomTalk.key, talks = current))
                current = mutableListOf(talk)
                prev = talk
            }
        }
        sessions.add(Session(room = roomTalk.key, talks = current))
        current = mutableListOf()
        prev = null
    }

    return sessions
}

private fun SessionDto.toModel(): Talk? {
    return when (format) {
        "presentation" -> Presentation(
            sessionId = sessionId,
            title = title,
            startTime = OffsetDateTime.parse(startTimeZulu),
            endTime = OffsetDateTime.parse(endTimeZulu),
            length = length,
            intendedAudience = intendedAudience,
            language = language,
            abstract = abstract,
            speakers = speakers.map { it.toModel() }.toSet(),
            video = video
        )
        "lightning-talk" -> Lightning(
            sessionId = sessionId,
            title = title,
            startTime = OffsetDateTime.parse(startTimeZulu),
            endTime = OffsetDateTime.parse(endTimeZulu),
            length = length,
            intendedAudience = intendedAudience,
            language = language,
            abstract = abstract,
            speakers = speakers.map { it.toModel() }.toSet(),
            video = video
        )
        "workshop" -> Workshop(
            sessionId = sessionId,
            title = title,
            startTime = OffsetDateTime.parse(startTimeZulu),
            endTime = OffsetDateTime.parse(endTimeZulu),
            length = length,
            intendedAudience = intendedAudience,
            language = language,
            abstract = abstract,
            speakers = speakers.map { it.toModel() }.toSet(),
            video = video
        )
        else -> {
            Log.e(TAG, "Unknown format: $format")
            null
        }
    }
}

private fun SpeakerDto.toModel(): Speaker =
    Speaker(
        name = name,
        bio = bio,
        avatar = pictureUrl,
        twitter = twitter
    )

