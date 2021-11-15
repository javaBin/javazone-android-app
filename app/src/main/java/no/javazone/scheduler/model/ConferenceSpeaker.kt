package no.javazone.scheduler.model

data class ConferenceSpeaker(
    val name: String,
    val bio: String,
    val twitter: String? = null,
    val avatarUrl: String? = null
)
