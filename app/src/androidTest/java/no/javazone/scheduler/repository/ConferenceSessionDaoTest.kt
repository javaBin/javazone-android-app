package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import no.javazone.scheduler.repository.room.Schedule
import no.javazone.scheduler.repository.room.ScheduleEntity
import no.javazone.scheduler.repository.room.TalkEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime

@RunWith(AndroidJUnit4::class)
class ConferenceSessionDaoTest {
    private lateinit var dao: ConferenceSessionDao
    private lateinit var db: AppDatabase

    private val room = TestUtil.createRoom("room 1")
    private val timeSlot1 = TestUtil.createTimeSlot(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1L))
    private val timeSlot2 = TestUtil.createTimeSlot(timeSlot1.endTime, timeSlot1.endTime.plusHours(1L))
    private val talk1: TalkEntity = TestUtil.createTalk("talk1", room, timeSlot1)
    private val talk2: TalkEntity = TestUtil.createTalk("talk2", room, timeSlot2)

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
    fun adding_and_deleting_schedule_works() = runBlocking {
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

    private suspend fun initDb() {
        dao.addRoom(room)
        dao.addTimeSlot(timeSlot1)
        dao.addTimeSlot(timeSlot2)
        dao.addTalk(talk1)
        dao.addTalk(talk2)
    }
}