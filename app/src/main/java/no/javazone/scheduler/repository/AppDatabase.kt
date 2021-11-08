package no.javazone.scheduler.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import no.javazone.scheduler.model.*
import no.javazone.scheduler.repository.impl.ConferenceSessionDao

@Database(
    entities = [
        ConferenceRoom::class,
        ConferenceSlot::class,
        Schedule::class,
        Speaker::class,
        Talk::class,
        TalkSpeakerCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sessionDao(): ConferenceSessionDao

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
            return Room.databaseBuilder(context, AppDatabase::class.java, "test")
                .addCallback(callback)
                .build()
        }
    }
}