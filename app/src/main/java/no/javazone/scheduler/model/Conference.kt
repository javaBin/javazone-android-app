package no.javazone.scheduler.model

import java.time.LocalDate

data class Conference(
    val name: String,
    val workshop: LocalDate,
    val days: List<LocalDate>,
    val conferenceUrl: String
)
