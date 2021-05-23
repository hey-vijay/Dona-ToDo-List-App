package vijay.app.dona.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vijay.app.dona.database.dao.TaskDao
import vijay.app.dona.database.modal.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class DonaDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    companion object {
        @Volatile
        private var noteDatabase: DonaDatabase? = null

        fun getInstance(context: Context): DonaDatabase {
            val tempDatabase = noteDatabase
            if (tempDatabase != null) return tempDatabase
            synchronized(this) {
                val instance: DonaDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    DonaDatabase::class.java,
                    "Dona_database"
                ).fallbackToDestructiveMigration().build()
                noteDatabase = instance
                instance.populateInitialNotes(context)
                return instance
            }
        }
    }

    private fun populateInitialNotes(context: Context) {    }
}