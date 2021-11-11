package no.javazone.scheduler.repository

import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.repository.room.RoomEntity
import no.javazone.scheduler.repository.room.TalkEntity
import no.javazone.scheduler.repository.room.TimeSlotEntity
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

object TestUtil {
    fun createRoom(name: String) =
        RoomEntity(roomId = kotlin.math.abs(name.hashCode()), name = name)

    fun createTimeSlot(startTime: OffsetDateTime, endTime: OffsetDateTime) =
        TimeSlotEntity(
            startTime = startTime,
            endTime = endTime
        )

    fun createTalk(title: String, room: RoomEntity, timeSlot: TimeSlotEntity) =
        TalkEntity(
            talkId = UUID.randomUUID().toString(),
            title = title,
            length = Duration.between(timeSlot.startTime, timeSlot.endTime).toMinutes().toInt(),
            intendedAudience = "random",
            language = "no",
            video = null,
            summary = "some abstract summary",
            format = ConferenceFormat.PRESENTATION,
            room = room.roomId,
            sessionSlot = timeSlot.timeSlotId,
            startTime = timeSlot.startTime,
            endTime = timeSlot.endTime
        )
}