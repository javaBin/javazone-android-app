package no.javazone.scheduler.model

import java.util.*

data class ConferenceRoom constructor(val key: String, val name: String) : Comparable<ConferenceRoom> {

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