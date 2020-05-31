package no.javazone.scheduler.model

import java.net.URL
import java.time.LocalDate

data class ConferenceConfig(
    val name: String,
    val workshopDay: LocalDate,
    val days: Set<LocalDate>,
    val url: URL) {
    constructor(
        name: String,
        workshopDay: LocalDate,
        days: Set<LocalDate>,
        url: String) : this(name = name, workshopDay = workshopDay, days = days, url = URL(url))
}
