package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "talk_speaker_crossref",
    primaryKeys = ["talk_id", "speaker_id"],
)
data class TalkSpeakerCrossRef(
    @ColumnInfo(name = "talk_id", index = true)
    val talkId: String,
    @ColumnInfo(name = "speaker_id", index = true)
    val speakerId: Long
)
