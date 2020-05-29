package no.javazone.scheduler.model

import java.util.*

data class Room private constructor(
    val name: String
) {
    companion object {
        private val rooms: MutableMap<String, Room> = mutableMapOf()

        fun create(name: String): Room {
            return rooms.getOrPut(name.toLowerCase(Locale.ROOT)) {
                Room(name.toLowerCase(Locale.ROOT))
            }
        }
    }
}