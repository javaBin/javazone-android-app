package no.javazone.scheduler.repository.room

import androidx.room.*
import androidx.room.ForeignKey.Companion.NO_ACTION

@Entity(
    tableName = "schedules",
    indices = [Index(name = "idx_schedule_talk_id", value = ["talk_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = TalkEntity::class,
            parentColumns = ["talk_id"],
            childColumns = ["talk_id"],
            deferred = true,
            onUpdate = NO_ACTION,
            onDelete = NO_ACTION
        )
    ]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    var scheduleId: Long = 0L,
    @ColumnInfo(name = "talk_id")
    val talkId: String
)
