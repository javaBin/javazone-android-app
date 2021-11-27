package no.javazone.scheduler.api

import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import no.javazone.scheduler.dto.ConferenceDto
import no.javazone.scheduler.dto.SessionDto
import no.javazone.scheduler.dto.SessionsDto
import no.javazone.scheduler.dto.SpeakerDto
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.utility.TestDispatchersProvider
import no.javazone.scheduler.utils.toJzString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RunWith(RobolectricTestRunner::class)
class NetworkConferenceSessionTest {
    private lateinit var api: ConferenceSessionApi

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        api = NetworkConferenceSession.getInstance(
            client = object : NetworkClient {
                override suspend fun getConference(): ConferenceDto =
                    ConferenceDto(
                        conferenceName = "JavaZone 2021",
                        workshopDate = LocalDate.now().toJzString(),
                        conferenceDates = listOf(
                            LocalDate.now().plusDays(1).toJzString(),
                            LocalDate.now().plusDays(2).toJzString()
                        ),
                        conferenceUrl = "http://localhost"
                    )

                override suspend fun getSessions(conference: String): SessionsDto =
                    SessionsDto(
                        sessions = listOf(
                            SessionDto(
                                intendedAudience = "public",
                                startTimeZulu = OffsetDateTime.now().format(
                                    DateTimeFormatter.ISO_DATE_TIME
                                ),
                                endTimeZulu = OffsetDateTime.now().plusMinutes(20L).format(
                                    DateTimeFormatter.ISO_DATE_TIME
                                ),
                                format = ConferenceFormat.PRESENTATION.name.lowercase(),
                                length = 20,
                                language = "no",
                                sessionId = UUID.randomUUID().toString(),
                                abstract = "blablablah",
                                title = "This or that",
                                room = "Room 1",
                                conferenceId = UUID.randomUUID().toString(),
                                speakers = listOf(
                                    SpeakerDto(
                                        name = "Test Testersen",
                                        bio = "blasdjasdad",
                                        pictureUrl = ""
                                    )
                                )
                            )
                        )
                    )
            },
            dispatchers = TestDispatchersProvider
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conferencesessions should work`() = runBlockingTest {
        val result = api.fetchSessions("")
        Truth.assertThat(result).isNotEmpty()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `fetching conference should work`() = runBlockingTest {
        val result = api.fetchConference()
        Truth.assertThat(result.days).isNotEmpty()
    }

}