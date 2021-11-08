package no.javazone.scheduler.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "rooms")
data class ConferenceRoom(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "name")
    val name: String
    ) : Comparable<ConferenceRoom> {

    override fun compareTo(other: ConferenceRoom): Int = key.compareTo(other.key)

    companion object {
        private val CONFERENCE_ROOMS: MutableMap<String, ConferenceRoom> = mutableMapOf()

        fun create(name: String): ConferenceRoom {
            val key = name.lowercase(Locale.getDefault())
            return CONFERENCE_ROOMS.getOrPut(key) {
                ConferenceRoom(key, name)
            }
        }
    }
}