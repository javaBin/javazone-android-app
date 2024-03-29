package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
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
    version = 8,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 4, to = 8, spec = AppDatabase.Migrate7To8::class),
        AutoMigration(from = 5, to = 8, spec = AppDatabase.Migrate7To8::class),
        AutoMigration(from = 6, to = 8, spec = AppDatabase.Migrate7To8::class),
        AutoMigration(from = 7, to = 8, spec = AppDatabase.Migrate7To8::class),
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    @DeleteColumn(
        tableName = "talks",
        columnName = "registration_link"
    )

    class Migrate7To8 : AutoMigrationSpec {

    }

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

