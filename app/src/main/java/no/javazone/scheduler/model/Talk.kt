package no.javazone.scheduler.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.time.OffsetDateTime


@Entity(
    tableName = "talks",
    foreignKeys = [
        ForeignKey(
            entity = ConferenceRoom::class,
            parentColumns = ["key"],
            childColumns = ["fk_room"],
            deferred = true
        ),
        ForeignKey(
            entity = ConferenceSlot::class,
            parentColumns = ["id"],
            childColumns = ["fk_session_slot"],
            deferred = true,
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ]
)
data class Talk(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "length")
    val length: Int,
    @ColumnInfo(name = "audience")
    val intendedAudience: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "video")
    val video: String?,
    @ColumnInfo(name = "abstract")
    val abstract: String,
    @ColumnInfo(name = "format")
    val format: ConferenceFormat,
    @ColumnInfo(name = "fk_room", index = true)
    val room: String,
    @ColumnInfo(name = "fk_session_slot", index = true)
    val sessionSlot: Int,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime
)
