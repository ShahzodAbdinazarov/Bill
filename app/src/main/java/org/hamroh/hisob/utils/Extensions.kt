package org.hamroh.hisob.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getStartOfMonth(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

fun Activity.closeKeyboard(editText: EditText) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Long.timeFormat(format: String = "dd-MMM, yyyy, HH:mm"): String = SimpleDateFormat(format, Locale.forLanguageTag("uz")).format(Date(this))

fun Double.moneyFormat(): String = NumberFormat.getCurrencyInstance(Locale("uz", "UZ")).format(this)

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

@SuppressLint("SimpleDateFormat")
fun Long.getDate(dateFormat: String?): String? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(dateFormat, Locale.forLanguageTag("uz")).format(calendar.time)
}

fun ArrayList<Transaction>.getDays(): ArrayList<DayModel> {

    val dayList: ArrayList<DayModel> = arrayListOf()
    var helperList: ArrayList<Transaction> = arrayListOf()
    if (this.size > 0) helperList.add(this[0])
    for (i in 1 until this.size) {
        if (this[i - 1].time.startDay() != this[i].time.startDay()) {
            dayList.add(DayModel(time = this[i - 1].time, transactions = helperList))
            helperList = arrayListOf()
        }
        helperList.add(this[i])
    }
    if (this.size > 1) dayList.add(DayModel(time = this[this.size - 1].time, transactions = helperList))

    return dayList
}

fun Long.startDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.HOUR_OF_DAY, 0) // Soatni 00:00 ga sozlaymiz
    calendar.set(Calendar.MINUTE, 0)      // Daqiqani 00 ga sozlaymiz
    calendar.set(Calendar.SECOND, 0)      // Sekuntda 00 ga sozlaymiz
    calendar.set(Calendar.MILLISECOND, 0) // Millisekundni ham 00 ga sozlaymiz

    return calendar.timeInMillis
}