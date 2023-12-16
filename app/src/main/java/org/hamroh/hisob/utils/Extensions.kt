package org.hamroh.hisob.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.timeFormat(dateFormat: String = "dd-MMM, yyyy, HH:mm"): String = SimpleDateFormat(dateFormat, Locale.forLanguageTag("uz")).format(Date(this))

fun Context.getTime(callback: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val timeInMillis = calendar.timeInMillis
            callback(timeInMillis) // Pass the timeInMillis using the callback
        }

        TimePickerDialog(
            this, timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()

    }
    DatePickerDialog(
        this, dateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}