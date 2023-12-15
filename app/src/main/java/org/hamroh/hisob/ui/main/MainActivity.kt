package org.hamroh.hisob.ui.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.Filter
import org.hamroh.hisob.data.History
import org.hamroh.hisob.databinding.ActivityMainBinding
import org.hamroh.hisob.ui.about.AboutActivity
import org.hamroh.hisob.utils.SharedPrefs
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor

@SuppressLint("SetTextI18n", "NewApi")
class MainActivity : AppCompatActivity(), OnDateSetListener, OnTimeSetListener {
    private val `is` = booleanArrayOf(true, true, true, true, true, true, true)
    private var history: ListView? = null
    private var money = 0.0
    private var amount: TextView? = null
    private var current: TextView? = null
    private var income: TextView? = null
    private var outcome: TextView? = null
    private var from: TextView? = null
    private var to: TextView? = null
    private var setText: TextView? = null
    private var year = 0
    private var month = 0
    private var day = 0
    private var db: DBHelper? = null
    private var cr: SharedPrefs? = null
    private var toTime: Long = 0
    private var fromTime: Long = 0
    private var selectTime = Calendar.getInstance().timeInMillis

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val expendLayout = findViewById<LinearLayout>(R.id.expendLayout)
        val btnQuestion = findViewById<ImageView>(R.id.btnQuestion)
        val expend = findViewById<ImageView>(R.id.expend)
        amount = findViewById(R.id.amount)
        current = findViewById(R.id.current)
        income = findViewById(R.id.income)
        outcome = findViewById(R.id.outcome)
        history = findViewById(R.id.history)
        from = findViewById(R.id.from)
        to = findViewById(R.id.to)
        db = DBHelper(this)
        cr = SharedPrefs(this)
        toTime = Calendar.getInstance().timeInMillis
        fromTime = cr!!.getFromTime()
        val time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(fromTime))
        binding.from.text = time
        refresh()
        fab.setOnClickListener { dialog() }
        binding.from.setOnClickListener {
            setTime()
            setSetText(from)
        }
        binding.to.setOnClickListener {
            setTime()
            setSetText(to)
        }
        btnQuestion.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
        expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        val more = booleanArrayOf(true)
        expend.setOnClickListener {
            if (more[0]) {
                expend.setImageResource(R.drawable.ic_expand_less)
                expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                expend.setImageResource(R.drawable.ic_expand_more)
                expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            }
            more[0] = !more[0]
        }
        clickExpends()
    }

    private fun clickExpends() {
        val zeroFilter = findViewById<ImageView>(R.id.zeroFilter)
        val oneFilter = findViewById<ImageView>(R.id.oneFilter)
        val twoFilter = findViewById<ImageView>(R.id.twoFilter)
        val threeFilter = findViewById<ImageView>(R.id.threeFilter)
        val fourFilter = findViewById<ImageView>(R.id.fourFilter)
        val fiveFilter = findViewById<ImageView>(R.id.fiveFilter)
        val sixFilter = findViewById<ImageView>(R.id.sixFilter)
        zeroFilter.setOnClickListener {
            if (`is`[0]) {
                zeroFilter.setColorFilter(Color.parseColor("#888888"))
                oneFilter.setColorFilter(Color.parseColor("#888888"))
                twoFilter.setColorFilter(Color.parseColor("#888888"))
                threeFilter.setColorFilter(Color.parseColor("#888888"))
                fourFilter.setColorFilter(Color.parseColor("#888888"))
                fiveFilter.setColorFilter(Color.parseColor("#888888"))
                sixFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[0] = false
                `is`[1] = false
                `is`[2] = false
                `is`[3] = false
                `is`[4] = false
                `is`[5] = false
                `is`[6] = false
            } else {
                zeroFilter.setColorFilter(Color.parseColor("#ff00ff"))
                oneFilter.setColorFilter(Color.parseColor("#10C040"))
                twoFilter.setColorFilter(Color.parseColor("#C01010"))
                threeFilter.setColorFilter(Color.parseColor("#DDDD22"))
                fourFilter.setColorFilter(Color.parseColor("#DDDD22"))
                fiveFilter.setColorFilter(Color.parseColor("#002299"))
                sixFilter.setColorFilter(Color.parseColor("#002299"))
                `is`[0] = true
                `is`[1] = true
                `is`[2] = true
                `is`[3] = true
                `is`[4] = true
                `is`[5] = true
                `is`[6] = true
            }
            refresh()
        }
        oneFilter.setOnClickListener {
            if (`is`[2]) {
                oneFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[2] = false
            } else {
                oneFilter.setColorFilter(Color.parseColor("#10C040"))
                `is`[2] = true
            }
            refresh()
        }
        twoFilter.setOnClickListener {
            if (`is`[1]) {
                twoFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[1] = false
            } else {
                twoFilter.setColorFilter(Color.parseColor("#C01010"))
                `is`[1] = true
            }
            refresh()
        }
        threeFilter.setOnClickListener {
            if (`is`[3]) {
                threeFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[3] = false
            } else {
                threeFilter.setColorFilter(Color.parseColor("#DDDD22"))
                `is`[3] = true
            }
            refresh()
        }
        fourFilter.setOnClickListener {
            if (`is`[4]) {
                fourFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[4] = false
            } else {
                fourFilter.setColorFilter(Color.parseColor("#DDDD22"))
                `is`[4] = true
            }
            refresh()
        }
        fiveFilter.setOnClickListener {
            if (`is`[5]) {
                fiveFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[5] = false
            } else {
                fiveFilter.setColorFilter(Color.parseColor("#002299"))
                `is`[5] = true
            }
            refresh()
        }
        sixFilter.setOnClickListener {
            if (`is`[6]) {
                sixFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[6] = false
            } else {
                sixFilter.setColorFilter(Color.parseColor("#002299"))
                `is`[6] = true
            }
            refresh()
        }
    }

    private fun dialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_type)
        val txtOutgoing = dialog.findViewById<TextView>(R.id.txtOutgoing)
        val txtIncome = dialog.findViewById<TextView>(R.id.txtIncome)
        val txtBorrow = dialog.findViewById<TextView>(R.id.txtBorrow)
        val txtBorrowBack = dialog.findViewById<TextView>(R.id.txtBorrowBack)
        val txtLend = dialog.findViewById<TextView>(R.id.txtLend)
        val txtLendBack = dialog.findViewById<TextView>(R.id.txtLendBack)
        txtOutgoing.setOnClickListener { showInputDialog(0, dialog) }
        txtIncome.setOnClickListener { showInputDialog(1, dialog) }
        txtBorrow.setOnClickListener { showInputDialog(2, dialog) }
        txtBorrowBack.setOnClickListener { showInputDialog(3, dialog) }
        txtLend.setOnClickListener { showInputDialog(4, dialog) }
        txtLendBack.setOnClickListener { showInputDialog(5, dialog) }
        dialog.show()
    }

    private fun showInputDialog(type: Int, d: Dialog) {
        d.cancel()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        val icon = dialog.findViewById<ImageView>(R.id.dialogImage)
        val title = dialog.findViewById<TextView>(R.id.dialogTitle)
        when (type) {
            0 -> {
                icon.setImageResource(R.drawable.down)
                title.setText(R.string.outgoing)
            }

            1 -> {
                icon.setImageResource(R.drawable.up)
                title.setText(R.string.income)
            }

            2 -> {
                icon.setImageResource(R.drawable.ic_borrow)
                title.setText(R.string.borrow)
            }

            3 -> {
                icon.setImageResource(R.drawable.ic_borrow_back)
                title.setText(R.string.borrow_back)
            }

            4 -> {
                icon.setImageResource(R.drawable.ic_lend)
                title.setText(R.string.lend)
            }

            5 -> {
                icon.setImageResource(R.drawable.ic_lend_back)
                title.setText(R.string.lend_back)
            }

            else -> {}
        }
        val dialogMoney = dialog.findViewById<EditText>(R.id.dialogMoney)
        val dialogTime = dialog.findViewById<TextView>(R.id.dialogTime)
        dialogTime.text = DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM,
            DateFormat.SHORT
        ).format(Date(Calendar.getInstance().timeInMillis))
        selectTime = Calendar.getInstance().timeInMillis
        dialogTime.setOnClickListener {
            setTime()
            setSetText(dialogTime)
        }
        val dialogInfo = dialog.findViewById<EditText>(R.id.dialogInfo)
        val dialogButton = dialog.findViewById<Button>(R.id.dialogOK)
        dialogButton.setOnClickListener {
            if (dialogMoney.text.toString().isNotEmpty()) {
                val edtMoney = dialogMoney.text.toString().toDouble()
                if (edtMoney < money || type == 1 || type == 2 || type == 5) {
                    val info = dialogInfo.text.toString()
                    val history = History(1, money = edtMoney, selectTime, info = info, type)
                    db!!.add(history)
                    dialog.dismiss()
                    recreate()
                } else {
                    dialogMoney.error = "Yetarli mablag' mavjud emas!"
                    dialogMoney.requestFocus()
                }
            } else {
                dialogMoney.error = "Mablag'ni kiriting!"
                dialogMoney.requestFocus()
            }
        }
        dialog.show()
    }

    private fun refresh() {
        val filter: Filter = db!!.getIncome(fromTime, toTime)
        var `in` = 0.0
        var out = 0.0
        `in` += filter.up
        out += filter.down
        `in` += filter.borrow
        out += filter.borrowBack
        out += filter.lend
        `in` += filter.lendBack
        money = `in` - out
        val day = floor(money / db!!.dailyAmount).toInt()
        amount!!.text = "Tahminan $day kunga yetadi."
        `in` = 0.0
        out = 0.0
        if (`is`[2]) {
            `in` += filter.up
        }
        if (`is`[1]) {
            out += filter.down
        }
        if (`is`[3]) {
            `in` += filter.borrow
        }
        if (`is`[4]) {
            out += filter.borrowBack
        }
        if (`is`[5]) {
            out += filter.lend
        }
        if (`is`[6]) {
            `in` += filter.lendBack
        }
        var decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        var txtMoney = decimalFormat.format(`in`)
        income!!.text = txtMoney
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        txtMoney = decimalFormat.format(out)
        outcome!!.text = txtMoney
        val number = `in` - out
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        txtMoney = decimalFormat.format(number)
        current!!.text = txtMoney
        val adapter = HistoryAdapter(this, db!!.getAll(fromTime, toTime, `is`))
        history!!.adapter = adapter
    }

    private fun setTime() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        day = dayOfMonth
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar[year, month, day, hourOfDay] = minute
        selectTime = calendar.timeInMillis
        val time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(selectTime))
        setText!!.text = time
        if (setText === from) {
            fromTime = selectTime
            cr!!.setFromTime(fromTime)
        } else if (setText === to) {
            toTime = selectTime
        }
        refresh()
    }

    private fun setSetText(setText: TextView?) {
        this.setText = setText
    }
}