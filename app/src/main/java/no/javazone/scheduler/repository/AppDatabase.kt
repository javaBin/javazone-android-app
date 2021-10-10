package no.javazone.scheduler.repository

//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import no.javazone.scheduler.repository.impl.ConferenceSessionDao

//@Database(entities = [ConferenceSession::class, Speaker::class, Talk::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDatabase //: RoomDatabase()
{

    abstract fun sessionDao(): ConferenceSessionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
//            return Room.databaseBuilder(context, AppDatabase::class.java, "test")
//                .addCallback(object: RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        val request = OneTimeWorkRequestBuilder<ConferenceUrlWorker>().build()
//                        WorkManager.getInstance(context).enqueue(request)
//                    }
//                })
//                .build()
            TODO()
        }
    }
}