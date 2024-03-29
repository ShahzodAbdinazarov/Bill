package org.hamroh.hisob.infra.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.transaction.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/*
fun getStartOfMonth(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}
*/

@SuppressLint("ClickableViewAccessibility")
fun TextView.extractAndSetClickableTags(desc: String, onTagClick: ((String) -> Unit)? = null, onNonTagClick: (() -> Unit)? = null) {
    val tagPattern = Regex("#\\w+")
    val spannableStringBuilder = SpannableStringBuilder(desc)
    tagPattern.findAll(desc).forEach { tagResult ->
        val tag = tagResult.value // Extract tag as a string
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onTagClick?.invoke(tag) // Pass tag to the lambda
            }
        }
        spannableStringBuilder.setSpan(
            clickableSpan,
            tagResult.range.first,
            tagResult.range.last + 1,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    text = spannableStringBuilder
    movementMethod = LinkMovementMethod.getInstance()

    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val layout = (v as TextView).layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val link = spannableStringBuilder.getSpans(off, off, ClickableSpan::class.java)
            if (link.isEmpty()) {
                onNonTagClick?.invoke()
            }
        }
        false
    }
}

fun ArrayList<Transaction>.doFilter(allFilter: AllFilter): ArrayList<DayModel> {
    val transactions = arrayListOf<Transaction>()
    val list = ArrayList(this)
    for (i in 0 until list.size) {
        if (list[i].time > allFilter.timeFilter.fromTime && list[i].time < allFilter.timeFilter.toTime) {
            when (list[i].type) {
                0 -> if (allFilter.typeFilter.expense) transactions.add(list[i])
                1 -> if (allFilter.typeFilter.income) transactions.add(list[i])
                2 -> if (allFilter.typeFilter.borrow) transactions.add(list[i])
                3 -> if (allFilter.typeFilter.borrowBack) transactions.add(list[i])
                4 -> if (allFilter.typeFilter.lending) transactions.add(list[i])
                else -> if (allFilter.typeFilter.lendingBack) transactions.add(list[i])
            }
        }
    }
    return transactions.getDays()
}

fun ArrayList<Transaction>.doTagFilter(allFilter: AllFilter): List<Transaction> {
    return filter { transaction ->
        transaction.time in allFilter.timeFilter.fromTime..allFilter.timeFilter.toTime && transaction.note.contains(allFilter.tag)
    }
}

fun String.getDouble(): Double {
    val filtered = this.filter { it.isDigit() || it == '.' }
    val amount = filtered.ifEmpty { "0" }
    return amount.toDouble()
}

fun EditText.etMoneyFormat() {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            this@etMoneyFormat.removeTextChangedListener(this)

            val amount = s.toString().replace(",", "")
            val parsedValue = try {
                val parsed = amount.getDouble()
                val approx = (parsed * 100).toULong()
                approx.toDouble() / 100
            } catch (e: NumberFormatException) {
                0.0
            }

            val formatted = if (parsedValue != 0.0) NumberFormat.getNumberInstance().format(parsedValue) else ""

            val finalResult = when {
                amount.startsWith(".") -> "0$formatted"
                amount.endsWith(".") -> "$formatted."
                amount.contains(".") -> "${formatted.substringBeforeLast('.')}.${amount.substringAfterLast('.').take(2)}"
                else -> formatted
            }

            this@etMoneyFormat.setText(finalResult)
            this@etMoneyFormat.setSelection(finalResult.length)

            this@etMoneyFormat.addTextChangedListener(this)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    addTextChangedListener(textWatcher)
}

fun Activity.showSoftKeyboard(view: EditText) {
    view.requestFocus()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_SHOWN)
    view.setSelection(view.length())
}

fun Activity.closeKeyboard(editText: EditText) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Long.timeFormat(format: String = "dd-MMM, yyyy, HH:mm"): String = SimpleDateFormat(format, Locale.forLanguageTag("uz")).format(Date(this))

fun Double.moneyFormat(): String = NumberFormat.getCurrencyInstance(Locale("uz", "UZ")).format(this)
fun ArrayList<DayModel>.getAmount(): Double {
    var currentAmount = 0.0
    for (i in 0 until this.size) {
        val ts = this[i].transactions
        for (j in 0 until ts.size)
            if (ts[j].type == 1 || ts[j].type == 2 || ts[j].type == 5) currentAmount += ts[j].amount
            else currentAmount -= ts[j].amount
    }
    return currentAmount
}

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

    var helperAmount = 0.0
    val dayList: ArrayList<DayModel> = arrayListOf()
    var helperList: ArrayList<Transaction> = arrayListOf()
    if (this.size > 0) helperList.add(this[0])
    for (i in 1 until this.size) {
        if (this[i - 1].time.startDay() != this[i].time.startDay()) {
            helperAmount = 0.0
            for (j in 0 until helperList.size) if (helperList[j].type == 0 || helperList[j].type == 3 || helperList[j].type == 4)
                helperAmount -= helperList[j].amount else helperAmount += helperList[j].amount
            dayList.add(DayModel(time = this[i - 1].time, transactions = helperList, amount = helperAmount))
            helperList = arrayListOf()
        }
        helperList.add(this[i])
    }
    for (j in 0 until helperList.size) if (helperList[j].type == 0 || helperList[j].type == 3 || helperList[j].type == 4)
        helperAmount -= helperList[j].amount else helperAmount += helperList[j].amount

    if (this.size > 0) dayList.add(DayModel(time = this[this.size - 1].time, transactions = helperList, amount = helperAmount))

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