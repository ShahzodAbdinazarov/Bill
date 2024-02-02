package org.hamroh.hisob.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.hamroh.hisob.data.transaction.Transaction

class AppMigration(private val context: Context) {

    fun migrateSQLiteToRoom(db: SupportSQLiteDatabase) {
        val sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("Bill"), null)
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM History", null)
        val dataList = mutableListOf<Transaction>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val amount = (cursor.getString(1).toDouble())
            val time = (cursor.getString(2).toLong())
            val note = (cursor.getString(3))
            val type = (cursor.getInt(4))

            val transaction = Transaction(id, amount, time, note, type)
            dataList.add(transaction)
        }

        cursor.close()

        // Insert data into the new version of the Room database
        for (transaction in dataList) {
            db.execSQL(
                "INSERT INTO transaction_table (amount, time, note, type) VALUES (?, ?, ?, ?)",
                arrayOf(transaction.amount, transaction.time, transaction.note, transaction.type)
            )
        }
    }
}