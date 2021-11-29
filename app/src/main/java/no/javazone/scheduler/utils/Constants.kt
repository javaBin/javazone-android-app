package no.javazone.scheduler.utils

import okhttp3.MediaType
import java.time.LocalDate

const val SESSIONS_FILENAME = "sessions.json"
const val CONFERENCE_FILENAME = "conference2019.json"
const val PARTNERS_FILENAME = "partners.json"
const val LOG_TAG = "JavaZone"
const val APP_PREFERENCE_FILE = "javazone"
const val JAVAZONE_BASE_URL = "https://sleepingpill.javazone.no/"
const val JAVAZONE_DATE_PATTERN = "dd.MM.yyyy"

val APPLICATION_JSON: MediaType = MediaType.parse("application/json")!!


val WORKSHOP_DAY: LocalDate = LocalDate.of(2021, 12, 7)
val FIRST_CONFERENCE_DAY: LocalDate = LocalDate.of(2021, 12, 8)

val DEFAULT_CONFERENCE_DAYS = listOf(
    WORKSHOP_DAY,
    FIRST_CONFERENCE_DAY,
    LocalDate.of(2021, 12, 9),
)
