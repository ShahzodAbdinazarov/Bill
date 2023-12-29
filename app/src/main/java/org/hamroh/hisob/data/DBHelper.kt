package org.hamroh.hisob.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Calendar
import java.util.Objects

class DBHelper internal constructor(context: Context?) : SQLiteOpenHelper(context, "Bill", null, 1) {
    private val db: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE History (" +
                    "id INTEGER PRIMARY KEY," +
                    "money TEXT," +
                    "time TEXT," +
                    "info TEXT," +
                    "isIncome INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS History")
        onCreate(db)
    }

    fun add(transaction: Transaction) {
        val values = ContentValues()
        values.put("money", transaction.amount.toString())
        values.put("time", transaction.time.toString())
        values.put("info", transaction.note)
        values.put("isIncome", transaction.type)
        db.insert("History", null, values)
    }

    fun update(transaction: Transaction) {
        val values = ContentValues()
        values.put("money", transaction.amount.toString())
        values.put("time", transaction.time.toString())
        values.put("info", transaction.note)
        values.put("isIncome", transaction.type)
        db.update("History", values, "id = ?", arrayOf(transaction.id.toString()))
    }

    fun delete(id: Int) {
        db.delete("History", "id = ?", arrayOf(id.toString()))
    }

    fun getAll(allFilter: AllFilter): List<Transaction> {
        val data: MutableList<Transaction> = ArrayList()
        val cursor = db.rawQuery("SELECT * FROM History ORDER BY time DESC", null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val transaction = Transaction()
                transaction.id = cursor.getInt(0)
                transaction.amount = (cursor.getString(1).toDouble())
                transaction.time = (cursor.getString(2).toLong())
                transaction.note = (cursor.getString(3))
                transaction.type = (cursor.getInt(4))
                if (cursor.getString(2).toLong() in (allFilter.timeFilter.fromTime + 1) until allFilter.timeFilter.toTime) {
                    when (cursor.getInt(4)) {
                        0 -> if (allFilter.typeFilter.expence) data.add(transaction)
                        1 -> if (allFilter.typeFilter.income) data.add(transaction)
                        2 -> if (allFilter.typeFilter.borrow) data.add(transaction)
                        3 -> if (allFilter.typeFilter.borrowBack) data.add(transaction)
                        4 -> if (allFilter.typeFilter.lending) data.add(transaction)
                        5 -> if (allFilter.typeFilter.lendingBack) data.add(transaction)
                    }
                }
            } while (cursor.moveToNext())
        }
        Objects.requireNonNull(cursor).close()
        return data
    }


    fun getIncome(allFilter: AllFilter): Filter {
        var up = 0.0
        var down = 0.0
        var borrow = 0.0
        var borrowBack = 0.0
        var lend = 0.0
        var lendBack = 0.0
        val cursor = db.rawQuery("SELECT * FROM History", null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val type = cursor.getInt(4)
                if (cursor.getString(2).toLong() in (allFilter.timeFilter.fromTime + 1) until allFilter.timeFilter.toTime
                ) {
                    when (type) {
                        0 -> down += cursor.getString(1).toDouble()
                        1 -> up += cursor.getString(1).toDouble()
                        2 -> borrow += cursor.getString(1).toDouble()
                        3 -> borrowBack += cursor.getString(1).toDouble()
                        4 -> lend += cursor.getString(1).toDouble()
                        5 -> lendBack += cursor.getString(1).toDouble()
                    }
                }
            } while (cursor.moveToNext())
        }
        Objects.requireNonNull(cursor).close()
        return Filter(up, down, borrow, borrowBack, lend, lendBack)
    }

    val dailyAmount: Double
        @SuppressLint("Recycle")
        get() {
            val currentTime = Calendar.getInstance().timeInMillis
            var time = currentTime
            val cursor = db.rawQuery("SELECT * FROM History ORDER BY time ASC", null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val type = cursor.getInt(4)
                    if (type == 0) {
                        time = cursor.getString(2).toLong()
                        break
                    }
                } while (cursor.moveToNext())
            }
            val (_, down) = getIncome(AllFilter(timeFilter = TimeFilter(0, currentTime)))
            return down / ((currentTime - time) / 86400000 + 1)
        }
}
