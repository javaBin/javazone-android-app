package no.javazone.scheduler.model

data class ConferenceRoom(val name: String) : Comparable<ConferenceRoom> {
    override fun compareTo(other: ConferenceRoom): Int =
        name.compareTo(other.name)

    companion object {
        val DEFAULT: ConferenceRoom = ConferenceRoom("")

        private val CONFERENCE_ROOMS: MutableMap<Int, ConferenceRoom> = mutableMapOf()

        fun create(name: String): ConferenceRoom {
            val key = name.hashCode()
            return CONFERENCE_ROOMS.getOrPut(key) {
                ConferenceRoom(name)
            }
        }
    }
}
