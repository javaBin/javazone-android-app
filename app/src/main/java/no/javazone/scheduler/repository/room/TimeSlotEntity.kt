package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

/**
 * id is the time in mmddhhmm format which is the starttime represented as a long
 * startTime is the StartTime for the session
 * endTime is the EndTime for the session
 */
@Entity(
    tableName = "time_slots",
)
data class TimeSlotEntity(
    @PrimaryKey
    @ColumnInfo(name = "time_slot_id")
    var timeSlotId: Long,
    @ColumnInfo(name = "month_day", index = true)
    val monthDay: Int,
    @ColumnInfo(name = "hour_minute", index = true)
    val hourMinute: Int,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime,
) {
    constructor(
        startTime: OffsetDateTime,
        endTime: OffsetDateTime
    ) : this(
        timeSlotId = "${startTime.monthValue}${startTime.dayOfMonth}${startTime.hour}${startTime.minute}".toLong(),
        monthDay = "${startTime.monthValue}${startTime.dayOfMonth}".toInt(),
        hourMinute = "${startTime.hour}${startTime.minute}".toInt(),
        startTime = startTime,
        endTime = endTime
    )
}