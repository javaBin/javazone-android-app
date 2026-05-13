package no.javazone.scheduler.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.time.LocalDate

const val SESSIONS_FILENAME = "sessions.json"
const val CONFERENCE_FILENAME = "conference.json"
const val PARTNERS_FILENAME = "partners.json"
const val LOG_TAG = "JavaZone"
const val APP_PREFERENCE_FILE = "javazone"
const val JAVAZONE_BASE_URL = "https://sleepingpill.javazone.no/"
const val JAVAZONE_DATE_PATTERN = "dd.MM.yyyy"
const val PREVIEW_CONFERENCE_NAME = "JavaZone 2026"
const val PREVIEW_CONFERENCE_DATES = "2–4 September 2026"
const val PREVIEW_WORKSHOP_DATE = "Tuesday, 2 September 2026"
const val CONFERENCE_LOCATION = "Oslo, Norway"

val APPLICATION_JSON: MediaType = "application/json".toMediaType()

val WORKSHOP_DAY: LocalDate = LocalDate.of(2019, 9, 10)
val FIRST_CONFERENCE_DAY: LocalDate = LocalDate.of(2019, 9, 11)
val LAST_CONFERENCE_DAY: LocalDate = LocalDate.of(2019, 9, 12)

val DEFAULT_CONFERENCE_DAYS = listOf(
    WORKSHOP_DAY,
    FIRST_CONFERENCE_DAY,
    LAST_CONFERENCE_DAY,
)
