package no.javazone.scheduler.model

import androidx.room.*
import java.time.OffsetDateTime


@Entity(
    tableName = "talks",
    indices = [Index(value = ["session_id"], name = "idx_talks_session_id")],
    foreignKeys = [
        ForeignKey(entity = Speaker::class, parentColumns = ["id"], childColumns = ["talk_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Talk(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime,
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
    @Relation(parentColumn = "id", entityColumn = "talk_id")
    val speakers: Set<Speaker>,
    @Embedded
    @ColumnInfo(name = "format")
    val format: ConferenceFormat) {

    @ColumnInfo(name = "session_id")
    var sessionId: Long = 0L
}