package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import no.javazone.scheduler.repository.room.Schedule
import no.javazone.scheduler.repository.room.ScheduleEntity
import no.javazone.scheduler.repository.room.TalkEntity
import no.javazone.scheduler.repository.room.TalkSpeakerCrossRef
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime

@RunWith(AndroidJUnit4::class)
class ConferenceDaoTest {
    private lateinit var dao: ConferenceDao
    private lateinit var db: AppDatabase

    private val room = TestUtil.createRoom("room 1")
    private val timeSlot1 = TestUtil.createTimeSlot(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1L))
    private val timeSlot2 = TestUtil.createTimeSlot(timeSlot1.endTime, timeSlot1.endTime.plusHours(1L))
    private val talk1: TalkEntity = TestUtil.createTalk("talk1", room, timeSlot1)
    private val talk2: TalkEntity = TestUtil.createTalk("talk2", room, timeSlot2)
    private val speaker1 = TestUtil.createSpeaker("Test Testersen")
    private val speaker2 = TestUtil.createSpeaker("Gunn Gunnersdottir")

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.sessionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun adding_and_deleting_schedule_works(): Unit = runBlocking {
        initDb()

        val schedule = ScheduleEntity(talkId = talk1.talkId)

        dao.addSchedule(schedule)
        dao.addSchedule(ScheduleEntity(talkId = talk2.talkId))

        var result = dao.getSchedules()
        assertThat(result).hasSize(2)

        dao.deleteSchedule(Schedule(talkId = talk1.talkId))

        result = dao.getSchedules()
        assertThat(result).hasSize(1)
    }

    /**
     * rowid and id is the same in sqlite
     * https://www.sqlitetutorial.net/sqlite-autoincrement/
     */
    @Test
    fun confirm_rowid_and_gen_id_is_same(): Unit = runBlocking {
        initDb()

        val rowId = dao.addSchedule(ScheduleEntity(talkId = talk2.talkId))
        val id = dao.getIdFromRowId(rowId)

        assertThat(id).isEqualTo(rowId)
    }

    @Test
    fun retrieve_sessions(): Unit = runBlocking {
        initDb()
        dao.addSchedule(ScheduleEntity(talkId = talk2.talkId))

        val sessionResult = dao.getConferenceSessions().first()
        assertThat(sessionResult).hasSize(2)

        val timeSlotResult = sessionResult
            .map {
                it.timeSlot
            }

        assertThat(timeSlotResult).containsExactly(timeSlot1, timeSlot2)

        val talkResults = sessionResult.flatMap {
            it.talks
        }
        assertThat(talkResults).hasSize(2)

        val talkResult2 = talkResults.firstOrNull { it.talkWithSpeakers.talk.talkId == talk2.talkId }
        assertThat(talkResult2?.scheduled).isNotNull()

        val talkResult1 = talkResults.firstOrNull { it.talkWithSpeakers.talk.talkId == talk1.talkId }
        assertThat(talkResult1?.scheduled).isNull()
    }

    private suspend fun initDb() {
        dao.addRooms(listOf(room))
        dao.addTimeSlots(listOf(timeSlot1, timeSlot2))
        listOf(talk1, talk2).forEach {
            dao.addTalk(it)
            var id = dao.addSpeaker(speaker1)
            dao.addTalkSpeaker(TalkSpeakerCrossRef(talkId = it.talkId, speakerId = id))
            id = dao.addSpeaker(speaker2)
            dao.addTalkSpeaker(TalkSpeakerCrossRef(talkId = it.talkId, speakerId = id))
        }
    }
}