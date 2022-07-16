package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.OffsetDateTime

/**
 * id is the time in mmddhhmm format which is the starttime represented as a long
 * startTime is the StartTime for the session in UTC
 * endTime is the EndTime for the session in UTC
 */
@Entity(
    tableName = "time_slots",
)
data class TimeSlotEntity(
    @PrimaryKey
    @ColumnInfo(name = "time_slot_id")
    var timeSlotId: Long,
    @ColumnInfo(name = "date", index = true)
    val date: LocalDate,
    @ColumnInfo(name = "hour_minute", index = true)
    val hourMinute: String,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime,
) {
    @Ignore
    constructor(
        startTime: OffsetDateTime,
        endTime: OffsetDateTime
    ) : this(
        timeSlotId = "${startTime.monthValue}${startTime.dayOfMonth}${startTime.hour}${startTime.minute}".toLong(),
        date = startTime.toLocalDate(),
        hourMinute = "${startTime.hour}-${startTime.minute}",
        startTime = startTime,
        endTime = endTime
    )
}