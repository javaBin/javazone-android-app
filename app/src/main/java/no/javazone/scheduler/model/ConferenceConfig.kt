package no.javazone.scheduler.model

import java.net.URL
import java.time.LocalDate

data class ConferenceConfig(
    val name: String,
    val days: Set<LocalDate>,
    val url: URL) {
    constructor(
        name: String,
        days: Set<LocalDate>,
        url: String) : this(name = name, days = days, url = URL(url))
}
