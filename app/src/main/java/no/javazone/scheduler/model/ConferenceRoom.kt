package no.javazone.scheduler.model

import java.util.*

data class ConferenceRoom constructor(val name: String) : Comparable<ConferenceRoom> {

    override fun compareTo(other: ConferenceRoom): Int = name.compareTo(other.name)

    companion object {
        private val CONFERENCE_ROOMS: MutableMap<String, ConferenceRoom> = mutableMapOf()

        fun create(name: String): ConferenceRoom {
            return CONFERENCE_ROOMS.getOrPut(name.toLowerCase(Locale.ROOT)) {
                ConferenceRoom(name.toLowerCase(Locale.ROOT))
            }
        }
    }
}