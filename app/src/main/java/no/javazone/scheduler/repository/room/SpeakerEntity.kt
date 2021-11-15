package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "speakers",
    indices = [Index(name = "idx_speaker_name", value = ["name"])]
)
data class SpeakerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "speaker_id")
    var speakerId: Long = 0L,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "bio")
    val bio: String,
    @ColumnInfo(name = "twitter")
    val twitter: String? = null,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null
)
