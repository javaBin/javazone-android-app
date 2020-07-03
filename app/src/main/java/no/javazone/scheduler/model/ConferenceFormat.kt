package no.javazone.scheduler.model

enum class ConferenceFormat {
    WORKSHOP,
    PRESENTATION,
    LIGHTNING_TALK
}

fun String.toConferenceFormat(): ConferenceFormat =
    when(this) {
        "workshop" -> ConferenceFormat.WORKSHOP
        "presentation" -> ConferenceFormat.PRESENTATION
        "lightning-talk" -> ConferenceFormat.LIGHTNING_TALK
        else -> throw RuntimeException("Unknown format")
    }