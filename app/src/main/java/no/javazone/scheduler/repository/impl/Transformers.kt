package no.javazone.scheduler.repository.impl

import no.javazone.scheduler.model.ConferenceRoom
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.ConferenceSpeaker
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.repository.room.*

fun toConferenceSession(): (Session) -> ConferenceSession = {
    ConferenceSession(
        time = it.timeSlot.startTime,
        talks = it.talks.map(toConferenceTalk())
    )
}

fun toConferenceTalk(): (RoomWithTalk) -> ConferenceTalk = {
    ConferenceTalk(
        id = it.talkWithSpeakers.talk.talkId,
        title = it.talkWithSpeakers.talk.title,
        length = it.talkWithSpeakers.talk.length,
        intendedAudience = it.talkWithSpeakers.talk.intendedAudience,
        language = it.talkWithSpeakers.talk.language,
        video = it.talkWithSpeakers.talk.video,
        summary = it.talkWithSpeakers.talk.summary,
        format = it.talkWithSpeakers.talk.format,
        room = ConferenceRoom.create(it.room.name),
        startTime = it.talkWithSpeakers.talk.startTime,
        endTime = it.talkWithSpeakers.talk.endTime,
        speakers = it.talkWithSpeakers.speakers.map(toConferenceSpeaker()).toSet(),
        scheduled = false
    )
}

fun toConferenceSpeaker(): (SpeakerEntity) -> ConferenceSpeaker = {
    ConferenceSpeaker(
        name = it.name,
        bio = it.bio,
        twitter = it.twitter,
        avatarUrl = it.avatarUrl
    )
}

fun ConferenceTalk.toEntity(room: RoomEntity, sessionSlot: TimeSlotEntity): TalkEntity =
    TalkEntity(
        talkId = this.id,
        title = this.title,
        length = this.length,
        intendedAudience = this.intendedAudience,
        language = this.language,
        video = this.video,
        summary = this.summary,
        format = this.format,
        room = room.roomId,
        sessionSlot = sessionSlot.timeSlotId,
        startTime = this.startTime,
        endTime = this.endTime
    )

fun ConferenceSpeaker.toEntity(): SpeakerEntity =
    SpeakerEntity(
        name = this.name,
        bio = this.bio,
        twitter = this.twitter,
        avatarUrl = this.avatarUrl
    )

