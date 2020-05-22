package no.javazone.scheduler.model

import java.net.URL
import java.time.LocalDate

data class ConferenceConfig(
    val name: String,
    val days: List<LocalDate>,
    val url: URL) {
    constructor(
        name: String,
        days: List<LocalDate>,
        url: String) : this(name = name, days = days, url = URL(url))
}
