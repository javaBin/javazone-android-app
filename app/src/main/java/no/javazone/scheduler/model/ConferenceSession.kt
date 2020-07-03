package no.javazone.scheduler.model

import androidx.room.*
import no.javazone.scheduler.BuildConfig
import java.time.OffsetDateTime

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(entity = Talk::class, parentColumns = ["id"], childColumns = ["session_id"])
    ]
)
data class ConferenceSession(
    @Embedded
    @ColumnInfo(name = "room")
    val room: ConferenceRoom,
    @Relation(parentColumn = "id", entityColumn = "session_id")
    val talks: List<Talk> = emptyList()) {
    init {
        if (BuildConfig.DEBUG && talks.isEmpty()) {
            error("session must have at least one talk")
        }
    }
    constructor(room: ConferenceRoom, talk: Talk) : this(room = room, talks = listOf(talk))

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L

    val startTime: OffsetDateTime
        get() = talks.map { it.startTime }.min() ?: OffsetDateTime.MIN

    val endTime: OffsetDateTime
        get() = talks.map { it.endTime }.max() ?: OffsetDateTime.MAX

    val length: Int
        get() = talks.map { it.length }.sum()
}