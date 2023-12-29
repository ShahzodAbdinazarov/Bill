package org.hamroh.hisob.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.hisob.R
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.Filter
import org.hamroh.hisob.databinding.FragmentHomeBinding
import org.hamroh.hisob.ui.main.add_transaction.AddTransactionDialog
import org.hamroh.hisob.ui.main.edit_transaction.EditTransactionDialog
import org.hamroh.hisob.ui.main.filter.FilterDialog
import org.hamroh.hisob.ui.main.profile.ProfileDialog
import org.hamroh.hisob.utils.SharedPrefs
import org.hamroh.hisob.utils.moneyFormat
import org.hamroh.hisob.utils.timeFormat

class HomeFragment : Fragment() {

    private var allFilter: AllFilter = AllFilter()
    private val viewModel: HomeViewModel by viewModels()

    private var currentAmount = 0.0
    private var db: DBHelper? = null
    private var cr: SharedPrefs? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        Log.e("TAG", "onCreateView: ${cr?.getFromTime()?.timeFormat()}")
        refresh()

        setupTransactionList()
        setupProfile()

        binding.fab.setOnClickListener {
            val addTransaction = AddTransactionDialog()
            addTransaction.currentAmount = currentAmount
            addTransaction.onClick = { refresh() }
            addTransaction.show(requireActivity().supportFragmentManager, "AddTransactionDialog")
        }

        binding.ibFilter.setOnClickListener {
            val filter = FilterDialog()
            filter.allFilter = allFilter
            filter.onClick = {
                allFilter = it
                refresh()
            }
            filter.show(requireActivity().supportFragmentManager, "FilterDialog")
        }

        /*

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
*/
        return binding.root
    }

    private fun setupProfile() {
        setName()
        binding.bnProfile.setOnClickListener {
            val profile = ProfileDialog()
            profile.onClick = { requireActivity().recreate() }
            profile.show(requireActivity().supportFragmentManager, "ProfileDialog")
        }
    }

    private fun setName() {
        val name = SharedPrefs(requireContext()).name
        binding.tvName.text = name.ifEmpty { getString(R.string.your_name) }
    }

    @SuppressLint("SetTextI18n")
    private fun refresh() {
        db = DBHelper(requireContext())
        cr = SharedPrefs(requireContext())
        val filter: Filter = db!!.getIncome(allFilter)
        var incomeAmount = 0.0
        var expenseAmount = 0.0
        incomeAmount += filter.income
        expenseAmount += filter.expence
        incomeAmount += filter.borrow
        expenseAmount += filter.borrowBack
        expenseAmount += filter.lending
        incomeAmount += filter.lendingBack
        currentAmount = incomeAmount - expenseAmount
//        val day = floor(money / db!!.dailyAmount).toInt()
//        binding.amount.text = "Tahminan $day kunga yetadi."
        incomeAmount = 0.0
        expenseAmount = 0.0
        if (allFilter.typeFilter.income) incomeAmount += filter.income
        if (allFilter.typeFilter.expence) expenseAmount += filter.expence
        if (allFilter.typeFilter.borrow) incomeAmount += filter.borrow
        if (allFilter.typeFilter.borrowBack) expenseAmount += filter.borrowBack
        if (allFilter.typeFilter.lending) expenseAmount += filter.lending
        if (allFilter.typeFilter.lendingBack) incomeAmount += filter.lendingBack
//        binding.income.text = `in`.moneyFormat()
//        binding.outcome.text = out.moneyFormat()
        binding.current.text = (incomeAmount - expenseAmount).moneyFormat()
        viewModel.getDays(ArrayList(db!!.getAll(allFilter)))
    }

    private fun setupTransactionList() {
        val transactions = DayAdapter {
            val editTransaction = EditTransactionDialog()
            editTransaction.currentAmount = currentAmount
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