package no.javazone.scheduler.model

import no.javazone.scheduler.R

enum class ConferenceFormat(val label: Int) {
    WORKSHOP(R.string.workshop),
    PRESENTATION(R.string.presentation),
    LIGHTNING_TALK(R.string.lightning_talk)
}

fun String.toConferenceFormat(): ConferenceFormat =
    when(this) {
        "workshop" -> ConferenceFormat.WORKSHOP
        "presentation" -> ConferenceFormat.PRESENTATION
        "lightning-talk" -> ConferenceFormat.LIGHTNING_TALK
        else -> throw RuntimeException("Unknown format")
    }