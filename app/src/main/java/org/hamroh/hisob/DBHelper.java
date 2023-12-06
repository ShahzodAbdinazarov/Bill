package org.hamroh.hisob;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.hamroh.hisob.Classes.Filter;
import org.hamroh.hisob.Classes.History;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@SuppressLint("Recycle")
public class DBHelper extends SQLiteOpenHelper {

    private final SQLiteDatabase db;

    DBHelper(Context context) {
        super(context, "Bill", null, 1);

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE History (" +
                "id INTEGER PRIMARY KEY," +
                "money TEXT," +
                "time TEXT," +
                "info TEXT," +
                "isIncome INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS History");
        onCreate(db);
    }

    void add(History history) {
        ContentValues values = new ContentValues();
        values.put("money", String.valueOf(history.getMoney()));
        values.put("time", String.valueOf(history.getTime()));
        values.put("info", history.getInfo());
        values.put("isIncome", history.getType());

        db.insert("History", null, values);
    }

    List<History> getAll(long fromTime, long toTime, boolean[] is) {
        List<History> data = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM History ORDER BY time DESC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                History history = new History();

                history.setId(cursor.getInt(0));
                history.setMoney(Double.parseDouble(cursor.getString(1)));
                history.setTime(Long.parseLong(cursor.getString(2)));
                history.setInfo(cursor.getString(3));
                history.setType(cursor.getInt(4));

                if (fromTime < Long.parseLong(cursor.getString(2)) &&
                        toTime > Long.parseLong(cursor.getString(2))) {
                    if (is[(cursor.getInt(4) + 1)])
                        data.add(history);
                }
            } while (cursor.moveToNext());
        }
        Objects.requireNonNull(cursor).close();
        return data;
    }

    void delete(int id) {
        db.delete("History", "id = ?", new String[]{String.valueOf(id)});
    }

    Filter getIncome(long fromTime, long toTime) {
        double up = 0;
        double down = 0;
        double borrow = 0;
        double borrowBack = 0;
        double lend = 0;
        double lendBack = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM History", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(4);

                if (fromTime < Long.parseLong(cursor.getString(2)) &&
                        toTime > Long.parseLong(cursor.getString(2))) {
                    switch (type) {
                        case 0 -> down += Double.parseDouble(cursor.getString(1));
                        case 1 -> up += Double.parseDouble(cursor.getString(1));
                        case 2 -> borrow += Double.parseDouble(cursor.getString(1));
                        case 3 -> borrowBack += Double.parseDouble(cursor.getString(1));
                        case 4 -> lend += Double.parseDouble(cursor.getString(1));
                        case 5 -> lendBack += Double.parseDouble(cursor.getString(1));
                    }
                }
            } while (cursor.moveToNext());
        }
        Objects.requireNonNull(cursor).close();
        return new Filter(up, down, borrow, borrowBack, lend, lendBack);
    }

    double getDailyAmount() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long time = currentTime;

        Cursor cursor = db.rawQuery("SELECT * FROM History ORDER BY time ASC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(4);

                if (type == 0) {
                    time = Long.parseLong(cursor.getString(2));
                    break;
                }

            } while (cursor.moveToNext());

        }
        Filter filter = getIncome(0, currentTime);
        return filter.getDown() / ((currentTime - time) / 86400000 + 1);
    }

}
