package no.javazone.scheduler.model

data class Conference(
    val name: String,
    val days: List<ConferenceDate>,
    val conferenceUrl: String
) {
    companion object {
        val NULL_INSTANCE = Conference(
            name = "",
            days = emptyList(),
            conferenceUrl = ""
        )
    }
}
