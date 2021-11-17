package no.javazone.scheduler.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "conferences"
)
data class ConferenceEntity(
    @PrimaryKey
    var id: Long = 0L,
    @ColumnInfo(name = "name", index = true)
    val name: String,
    @ColumnInfo(name = "url")
    val url: String
)
