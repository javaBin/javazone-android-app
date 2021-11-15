package no.javazone.scheduler.model

import java.time.OffsetDateTime

data class ConferenceTimeSlot(
    var timeSlotId: Long,
    val monthDay: String,
    val hourMinute: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime
)
