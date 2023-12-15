package org.hamroh.hisob.ui.main.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.Filter
import org.hamroh.hisob.data.History
import org.hamroh.hisob.databinding.FragmentHomeBinding
import org.hamroh.hisob.ui.about.AboutActivity
import org.hamroh.hisob.ui.main.HistoryAdapter
import org.hamroh.hisob.utils.SharedPrefs
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor

class HomeFragment : Fragment() {
    private val `is` = booleanArrayOf(true, true, true, true, true, true, true)
    private var money = 0.0
    private var setText: TextView? = null
    private var db: DBHelper? = null
    private var cr: SharedPrefs? = null
    private var toTime: Long = 0
    private var fromTime: Long = 0
    private var selectTime = Calendar.getInstance().timeInMillis

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = DBHelper(requireContext())
        cr = SharedPrefs(requireContext())
        toTime = Calendar.getInstance().timeInMillis
        fromTime = cr!!.getFromTime()

        val time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(fromTime))
        binding.from.text = time
        refresh()
        binding.fab.setOnClickListener { dialog() }
        binding.from.setOnClickListener {
            setTime()
            setSetText(binding.from)
        }
        binding.to.setOnClickListener {
            setTime()
            setSetText(binding.to)
        }
        binding.btnQuestion.setOnClickListener { startActivity(Intent(requireContext(), AboutActivity::class.java)) }
        binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        val more = booleanArrayOf(true)
        binding.expend.setOnClickListener {
            if (more[0]) {
                binding.expend.setImageResource(R.drawable.ic_expand_less)
                binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                binding.expend.setImageResource(R.drawable.ic_expand_more)
                binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            }
            more[0] = !more[0]
        }
        clickExpends()

        return binding.root
    }

    private fun clickExpends() {
        binding.zeroFilter.setOnClickListener {
            if (`is`[0]) {
                binding.zeroFilter.setColorFilter(Color.parseColor("#888888"))
                binding.oneFilter.setColorFilter(Color.parseColor("#888888"))
                binding.twoFilter.setColorFilter(Color.parseColor("#888888"))
                binding.threeFilter.setColorFilter(Color.parseColor("#888888"))
                binding.fourFilter.setColorFilter(Color.parseColor("#888888"))
                binding.fiveFilter.setColorFilter(Color.parseColor("#888888"))
                binding.sixFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[0] = false
                `is`[1] = false
                `is`[2] = false
                `is`[3] = false
                `is`[4] = false
                `is`[5] = false
                `is`[6] = false
            } else {
                binding.zeroFilter.setColorFilter(Color.parseColor("#ff00ff"))
                binding.oneFilter.setColorFilter(Color.parseColor("#10C040"))
                binding.twoFilter.setColorFilter(Color.parseColor("#C01010"))
                binding.threeFilter.setColorFilter(Color.parseColor("#DDDD22"))
                binding.fourFilter.setColorFilter(Color.parseColor("#DDDD22"))
                binding.fiveFilter.setColorFilter(Color.parseColor("#002299"))
                binding.sixFilter.setColorFilter(Color.parseColor("#002299"))
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
        binding.oneFilter.setOnClickListener {
            if (`is`[2]) {
                binding.oneFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[2] = false
            } else {
                binding.oneFilter.setColorFilter(Color.parseColor("#10C040"))
                `is`[2] = true
            }
            refresh()
        }
        binding.twoFilter.setOnClickListener {
            if (`is`[1]) {
                binding.twoFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[1] = false
            } else {
                binding.twoFilter.setColorFilter(Color.parseColor("#C01010"))
                `is`[1] = true
            }
            refresh()
        }
        binding.threeFilter.setOnClickListener {
            if (`is`[3]) {
                binding.threeFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[3] = false
            } else {
                binding.threeFilter.setColorFilter(Color.parseColor("#DDDD22"))
                `is`[3] = true
            }
            refresh()
        }
        binding.fourFilter.setOnClickListener {
            if (`is`[4]) {
                binding.fourFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[4] = false
            } else {
                binding.fourFilter.setColorFilter(Color.parseColor("#DDDD22"))
                `is`[4] = true
            }
            refresh()
        }
        binding.fiveFilter.setOnClickListener {
            if (`is`[5]) {
                binding.fiveFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[5] = false
            } else {
                binding.fiveFilter.setColorFilter(Color.parseColor("#002299"))
                `is`[5] = true
            }
            refresh()
        }
        binding.sixFilter.setOnClickListener {
            if (`is`[6]) {
                binding.sixFilter.setColorFilter(Color.parseColor("#888888"))
                `is`[6] = false
            } else {
                binding.sixFilter.setColorFilter(Color.parseColor("#002299"))
                `is`[6] = true
            }
            refresh()
        }
    }

    private fun dialog() {
        val dialog = Dialog(requireContext())
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
        val dialog = Dialog(requireContext())
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
                    refresh()
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

    @SuppressLint("SetTextI18n")
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
        binding.amount.text = "Tahminan $day kunga yetadi."
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
        binding.income.text = txtMoney
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        txtMoney = decimalFormat.format(out)
        binding.outcome.text = txtMoney
        val number = `in` - out
        decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        txtMoney = decimalFormat.format(number)
        binding.current.text = txtMoney
        val adapter = HistoryAdapter(requireActivity(), db!!.getAll(fromTime, toTime, `is`))
        binding.history.adapter = adapter
    }

    private fun setTime() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                selectTime = calendar.timeInMillis
                val time = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(selectTime))
                setText!!.text = time
                if (setText === binding.from) {
                    fromTime = selectTime
                    cr!!.setFromTime(fromTime)
                } else if (setText === binding.to) {
                    toTime = selectTime
                }
                refresh()
            }

            TimePickerDialog(
                requireContext(), timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()

        }
        DatePickerDialog(
            requireContext(), dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun setSetText(setText: TextView?) {
        this.setText = setText
    }
}