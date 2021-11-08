package no.javazone.scheduler.model

import androidx.room.ColumnInfo
import androidx.room.Junction
import androidx.room.Relation
import java.time.OffsetDateTime

data class SessionTalk(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "length")
    val length: Int,
    @ColumnInfo(name = "format")
    val format: ConferenceFormat,
    @Relation(
        parentColumn = "id",
        entityColumn = "talk_id"
    )
    val room: ConferenceRoom,
    @ColumnInfo(name = "start_time")
    val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time")
    val endTime: OffsetDateTime,
    @Relation(
        parentColumn = "id",
        entityColumn = "talk_id"
    )
    val mySchedule: Schedule?,
    @Relation(
        parentColumn = "talk_id",
        entityColumn = "speaker_id",
        associateBy = Junction(TalkSpeakerCrossRef::class)
    )
    val speakers: List<Speaker>
)
