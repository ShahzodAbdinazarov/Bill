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

    fun add(history: History) {
        val values = ContentValues()
        values.put("money", history.money.toString())
        values.put("time", history.time.toString())
        values.put("info", history.info)
        values.put("isIncome", history.type)
        db.insert("History", null, values)
    }

    fun getAll(fromTime: Long, toTime: Long, `is`: BooleanArray): List<History> {
        val data: MutableList<History> = ArrayList()
        val cursor = db.rawQuery("SELECT * FROM History ORDER BY time DESC", null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val history = History()
                history.id = cursor.getInt(0)
                history.money = (cursor.getString(1).toDouble())
                history.time = (cursor.getString(2).toLong())
                history.info = (cursor.getString(3))
                history.type = (cursor.getInt(4))
                if (cursor.getString(2).toLong() in (fromTime + 1) until toTime) {
                    if (`is`[cursor.getInt(4) + 1]) data.add(history)
                }
            } while (cursor.moveToNext())
        }
        Objects.requireNonNull(cursor).close()
        return data
    }

    fun delete(id: Int) {
        db.delete("History", "id = ?", arrayOf(id.toString()))
    }

    fun getIncome(fromTime: Long, toTime: Long): Filter {
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
                if (cursor.getString(2).toLong() in (fromTime + 1) until toTime
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
            val (_, down) = getIncome(0, currentTime)
            return down / ((currentTime - time) / 86400000 + 1)
        }
}
