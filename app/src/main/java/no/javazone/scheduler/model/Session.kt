package no.javazone.scheduler.model

data class Session(
    val room: String,
    val talks: List<Talk>
)