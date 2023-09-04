package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.*
import no.javazone.scheduler.repository.room.*
import no.javazone.scheduler.utils.APP_PREFERENCE_FILE

@Database(
    entities = [
        ConferenceEntity::class,
        ConferenceDateEntity::class,
        RoomEntity::class,
        TimeSlotEntity::class,
        ScheduleEntity::class,
        SpeakerEntity::class,
        TalkEntity::class,
        TalkSpeakerCrossRef::class
    ],
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
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
                .fallbackToDestructiveMigrationFrom(1, 2, 3, 4, 5)
                .addCallback(callback)
                .build()
        }

    }
}