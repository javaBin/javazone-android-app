package no.javazone.scheduler.model

import java.time.LocalDate

data class ConferenceDate(
    val date: LocalDate,
    val label: String
) : Comparable<ConferenceDate> {
    override fun compareTo(other: ConferenceDate): Int {
        val comp = label.compareTo(other.label)
        return if (comp == 0) {
            date.compareTo(other.date)
        } else {
            comp
        }
    }
}
