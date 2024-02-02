package org.hamroh.hisob.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.hamroh.hisob.R
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.databinding.FragmentHomeBinding
import org.hamroh.hisob.infra.utils.SharedPrefs
import org.hamroh.hisob.infra.utils.moneyFormat
import org.hamroh.hisob.ui.main.add_transaction.AddTransactionDialog
import org.hamroh.hisob.ui.main.edit_transaction.EditTransactionDialog
import org.hamroh.hisob.ui.main.filter.FilterDialog
import org.hamroh.hisob.ui.main.profile.ProfileDialog

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var allFilter = MutableLiveData<AllFilter>().apply { value = AllFilter() }
    private val viewModel: HomeViewModel by viewModels()

    private var currentAmount = 0.0

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
            filter.allFilter = allFilter.value!!
            filter.onClick = { allFilter.postValue(it) }
            filter.show(requireActivity().supportFragmentManager, "FilterDialog")
        }

        return binding.root
    }

    private fun refresh() = allFilter.observe(viewLifecycleOwner) { viewModel.getAllData(it) }

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

    private fun setupTransactionList() {
        val transactions = DayAdapter {
            val editTransaction = EditTransactionDialog()
            editTransaction.currentAmount = currentAmount
            editTransaction.transaction = it
            editTransaction.onClick = { refresh() }
            editTransaction.show(requireActivity().supportFragmentManager, "AddTransactionDialog")
        }
        viewModel.days.observe(viewLifecycleOwner) {
            transactions.submitList(it)
            currentAmount = 0.0
            for (i in 0 until it.size) {
                val ts = it[i].transactions
                for (j in 0 until ts.size)
                    if (ts[j].type == 1 || ts[j].type == 2 || ts[j].type == 5) currentAmount += ts[j].amount
                    else currentAmount -= ts[j].amount
            }
            binding.current.text = currentAmount.moneyFormat()
            binding.rvTransaction.post { binding.rvTransaction.smoothScrollToPosition(0) }
        }
        binding.rvTransaction.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = transactions
        }
    }

}