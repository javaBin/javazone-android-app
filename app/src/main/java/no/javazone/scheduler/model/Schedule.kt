package no.javazone.scheduler.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "schedules",
    indices = [Index(name = "idx_schedule_talk_id", value = ["fk_talk_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Talk::class,
            parentColumns = ["id"],
            childColumns = ["fk_talk_id"],
            deferred = true,
            onUpdate = CASCADE,
            onDelete = CASCADE)
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "fk_talk_id")
    val talkId: String
)
