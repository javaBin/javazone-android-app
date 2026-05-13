package no.javazone.scheduler.model

import no.javazone.scheduler.R

enum class ConferenceLanguage(val label: Int, val apiValue: String) {
    ENGLISH(R.string.language_english, "en"),
    NORWEGIAN(R.string.language_norwegian, "no")
}

fun String.toConferenceLanguage(): ConferenceLanguage? =
    ConferenceLanguage.entries.firstOrNull {
        it.apiValue.equals(this, ignoreCase = true)
    }
