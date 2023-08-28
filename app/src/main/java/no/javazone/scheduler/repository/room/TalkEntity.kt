package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import no.javazone.scheduler.model.ConferenceFormat
import java.time.OffsetDateTime


@Entity(
    tableName = "talks",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["room_id"],
            childColumns = ["fk_room"],
            deferred = true
        ),
        ForeignKey(
            entity = TimeSlotEntity::class,
            parentColumns = ["time_slot_id"],
            childColumns = ["fk_session_slot"],
            deferred = true,
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ]
)
data class TalkEntity(
    @PrimaryKey
    @ColumnInfo(name = "talk_id")
    val talkId: String,
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
    val summary: String,
    @ColumnInfo(name = "format")
    val format: ConferenceFormat,
    @ColumnInfo(name = "fk_room", index = true)
    val room: Int,
    @ColumnInfo(name = "fk_session_slot", index = true)
    val sessionSlot: Long,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime,
    @ColumnInfo(name = "registration_link")
    val registrationLink: String?
)
