package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import no.javazone.scheduler.repository.room.*
import no.javazone.scheduler.utils.APP_PREFERENCE_FILE

@Database(
    entities = [
        RoomEntity::class,
        TimeSlotEntity::class,
        ScheduleEntity::class,
        SpeakerEntity::class,
        TalkEntity::class,
        TalkSpeakerCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sessionDao(): ConferenceDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(
            context: Context,
            callback: Callback = object : RoomDatabase.Callback() {}
        ): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, APP_PREFERENCE_FILE)
                .addCallback(callback)
                .build()
        }
    }
}