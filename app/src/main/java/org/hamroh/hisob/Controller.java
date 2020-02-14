package org.hamroh.hisob;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class Controller {

    private SharedPreferences shp;

    Controller(Context context) {
        shp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    long getFromTime() {
        return shp.getLong("fromTime", 0);
    }

    void setFromTime(long fromTime) {
        shp.edit().putLong("fromTime", fromTime).apply();
    }

}
