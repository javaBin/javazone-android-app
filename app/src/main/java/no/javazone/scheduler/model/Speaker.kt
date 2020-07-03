package no.javazone.scheduler.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.net.URL

@Entity(
    tableName = "speakers",
    indices = [Index(name = "idx_speaker_talk_id", value = ["talk_id"])]
)
data class Speaker(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "bio")
    val bio: String,
    @ColumnInfo(name = "avatar")
    val avatar: String? = null,
    @ColumnInfo(name = "twitter")
    val twitter: String? = null) {
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0L

    @ColumnInfo(name = "talk_id")
    var talkId: String = ""

    val avatarUrl: URL?
        get() = avatar?.let { URL(avatar) }
}
