package org.hamroh.hisob.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.data.transaction.TransactionDao
import kotlin.concurrent.Volatile

@Database(entities = [Transaction::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).allowMainThreadQueries().addMigrations(MIGRATION_2_3(context)).build()
                INSTANCE = instance
                instance
            }
        }

        private fun MIGRATION_2_3(context: Context): Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) = AppMigration(context).migrateSQLiteToRoom(db)
        }
    }

}