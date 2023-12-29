package org.hamroh.hisob.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class SharedPrefs(context: Context) {

    companion object {
        private const val PREF = "MyAppPrefName"
        private const val FROM_TIME = "FROM_TIME"
        private const val NAME = "NAME"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun setName(name: String) = put(NAME, name)
    val name = get(NAME, String::class.java)

    fun getFromTime(): Long = get(FROM_TIME, Long::class.java)
    fun setFromTime(fromTime: Long) = put(FROM_TIME, fromTime)

    fun <T> get(key: String, clazz: Class<T>): T = when (clazz) {
        String::class.java -> sharedPref.getString(key, "")
        Boolean::class.java -> sharedPref.getBoolean(key, false)
        Float::class.java -> sharedPref.getFloat(key, 0f)
        Double::class.java -> sharedPref.getFloat(key, 0f)
        Int::class.java -> sharedPref.getInt(key, 0)
        Long::class.java -> sharedPref.getLong(key, 0L)
        else -> null
    } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

//    fun clearAll() = sharedPref.edit().clear().commit()

}
