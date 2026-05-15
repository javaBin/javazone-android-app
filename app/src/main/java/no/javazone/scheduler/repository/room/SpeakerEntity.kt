package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "speakers"
)
data class SpeakerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "speaker_id")
    var speakerId: Long = 0L,
    @ColumnInfo(name = "name", index = true)
    val name: String,
    @ColumnInfo(name = "bio")
    val bio: String,
    @ColumnInfo(name = "twitter")
    val twitter: String? = null,
    @ColumnInfo(name = "bluesky")
    val bluesky: String? = null,
    @ColumnInfo(name = "linkedin")
    val linkedin: String? = null
)
