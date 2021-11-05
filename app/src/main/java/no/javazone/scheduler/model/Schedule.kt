package no.javazone.scheduler.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "schedules",
    indices = [Index(name = "idx_schedule_talk_id", value = ["talk_id"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = Talk::class, parentColumns = ["id"], childColumns = ["talkId"], deferred = true)
    ]
)
data class Schedule(
    @ColumnInfo(name = "talk_id")
    var talkId: String
)
