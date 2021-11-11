package no.javazone.scheduler.model

import android.util.Log
import no.javazone.scheduler.dto.SessionDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.dto.SpeakerDto
import no.javazone.scheduler.utils.LOG_TAG
import java.time.OffsetDateTime


fun SessionsDto.toModel(): List<ConferenceSession> = convertDtoSessions(sessions)

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
        it.value.sortWith(Comparator { o1, o2 -> o1.startTime.compareTo(o2.startTime) })
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
            if (prev.endTime.isEqual(talk.startTime)) {
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
            startTime = OffsetDateTime.parse(startTimeZulu),
            endTime = OffsetDateTime.parse(endTimeZulu),
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

private fun toConferenceSpeaker(): (SpeakerDto) -> ConferenceSpeaker = {
    ConferenceSpeaker(
        name = it.name,
        bio = it.bio,
        avatarUrl = it.pictureUrl,
        twitter = it.twitter
    )
}

