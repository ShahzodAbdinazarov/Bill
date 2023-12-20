package org.hamroh.hisob.ui.main.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.Filter
import org.hamroh.hisob.databinding.FragmentHomeBinding
import org.hamroh.hisob.ui.main.add_transaction.AddTransactionDialog
import org.hamroh.hisob.ui.main.edit_transaction.EditTransactionDialog
import org.hamroh.hisob.utils.SharedPrefs
import org.hamroh.hisob.utils.getTime
import org.hamroh.hisob.utils.moneyFormat
import org.hamroh.hisob.utils.timeFormat
import java.util.Calendar
import kotlin.math.floor

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private val `is` = booleanArrayOf(true, true, true, true, true, true, true)
    private var money = 0.0
    private var db: DBHelper? = null
    private var cr: SharedPrefs? = null
    private var toTime: Long = 0
    private var fromTime: Long = 0

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        refresh()

        setupTransactionList()

        binding.from.text = fromTime.timeFormat()
        binding.fab.setOnClickListener {
            val addTransaction = AddTransactionDialog()
            addTransaction.money = money
            addTransaction.onClick = { refresh() }
            addTransaction.show(requireActivity().supportFragmentManager, "AddTransactionDialog")
        }
        binding.from.setOnClickListener {
            requireContext().getTime {
                fromTime = it
                cr!!.setFromTime(fromTime)
                binding.from.text = fromTime.timeFormat()
                refresh()
            }
        }
        binding.to.setOnClickListener {
            requireContext().getTime {
                toTime = it
                binding.to.text = toTime.timeFormat()
                refresh()
            }
        }
        binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        var more = true
        binding.expend.setOnClickListener {
            if (more) {
                binding.expend.setImageResource(R.drawable.ic_expand_less)
                binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                binding.expend.setImageResource(R.drawable.ic_expand_more)
                binding.expendLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            }
            more = !more
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

    @SuppressLint("SetTextI18n")
    private fun refresh() {
        db = DBHelper(requireContext())
        cr = SharedPrefs(requireContext())
        toTime = Calendar.getInstance().timeInMillis
        fromTime = cr!!.getFromTime()
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
        if (`is`[2]) `in` += filter.up
        if (`is`[1]) out += filter.down
        if (`is`[3]) `in` += filter.borrow
        if (`is`[4]) out += filter.borrowBack
        if (`is`[5]) out += filter.lend
        if (`is`[6]) `in` += filter.lendBack
        binding.income.text = `in`.moneyFormat()
        binding.outcome.text = out.moneyFormat()
        binding.current.text = (`in` - out).moneyFormat()
        viewModel.getDays(ArrayList(db!!.getAll(fromTime, toTime, `is`)))
    }

    private fun setupTransactionList() {
        val transactions = DayAdapter {
            val editTransaction = EditTransactionDialog()
            editTransaction.money = money
            editTransaction.transaction = it
            editTransaction.onClick = { refresh() }
            editTransaction.show(requireActivity().supportFragmentManager, "AddTransactionDialog")
        }
        viewModel.days.observe(viewLifecycleOwner) { transactions.submitList(it) }
        binding.rvTransaction.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = transactions
        }
    }

}