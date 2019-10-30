package uz.qmii.bill;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

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
        values.put("time", history.getTime());
        values.put("info", history.getInfo());
        values.put("isIncome", history.isIncome() ? 1 : 0);

        db.insert("History", null, values);
    }

    @SuppressLint("Recycle")
    List<History> getAll() {
        List<History> data = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM History", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                History history = new History();

                history.setId(cursor.getInt(0));
                history.setMoney(Double.parseDouble(cursor.getString(1)));
                history.setTime(cursor.getString(2));
                history.setInfo(cursor.getString(3));
                history.setIncome(cursor.getInt(4) == 1);

                data.add(history);
            } while (cursor.moveToNext());
        }
        Objects.requireNonNull(cursor).close();
        return data;
    }

    void delete(int id) {
        db.delete("History", "id = ?", new String[]{String.valueOf(id)});
    }

    public double getIncome(boolean is){
        double income = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM History", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (is == (cursor.getInt(4) == 1))
                income += Double.parseDouble(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Objects.requireNonNull(cursor).close();
        return income;
    }

}
