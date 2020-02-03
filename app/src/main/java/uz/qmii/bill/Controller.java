package uz.qmii.bill;

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

    boolean getFilter() {
        return shp.getBoolean("filter", false);
    }

    void setFilter(boolean b) {
        shp.edit().putBoolean("filter", b).apply();
    }
}
