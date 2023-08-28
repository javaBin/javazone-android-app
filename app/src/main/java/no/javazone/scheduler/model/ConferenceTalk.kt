package no.javazone.scheduler.model

import java.time.OffsetDateTime

data class ConferenceTalk(
    val id: String,
    val title: String,
    val length: Int,
    val intendedAudience: String,
    val language: String,
    val video: String?,
    val summary: String,
    val format: ConferenceFormat,
    val room: ConferenceRoom,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
    val speakers: Set<ConferenceSpeaker>,
    val scheduled: Boolean = false,
    val registrationLink: String?
) : Comparable<ConferenceTalk> {
    var slotTime: OffsetDateTime = startTime

    override fun compareTo(other: ConferenceTalk): Int {
        var result = slotTime.compareTo(other.slotTime)
        if (result != 0) return result

        result = room.compareTo(other.room)
        if (result != 0) return result

        return startTime.compareTo(other.startTime)
    }

}
