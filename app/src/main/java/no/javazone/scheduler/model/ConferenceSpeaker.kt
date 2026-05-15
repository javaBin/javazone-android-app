package no.javazone.scheduler.model

data class ConferenceSpeaker(
    val name: String,
    val bio: String,
    val twitter: String? = null,
    val bluesky: String? = null,
    val linkedin: String? = null
)
