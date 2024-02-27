package org.hamroh.hisob.ui.main.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.hamroh.hisob.R
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.databinding.FragmentTagBinding
import org.hamroh.hisob.infra.utils.doTagFilter
import org.hamroh.hisob.infra.utils.getAmount
import org.hamroh.hisob.infra.utils.getDays
import org.hamroh.hisob.infra.utils.getTime
import org.hamroh.hisob.infra.utils.moneyFormat
import org.hamroh.hisob.infra.utils.timeFormat
import org.hamroh.hisob.ui.main.home.HomeViewModel
import org.hamroh.hisob.ui.main.home.TransactionAdapter

@AndroidEntryPoint
class TagFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentTagBinding? = null
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var allFilter = AllFilter()
    private var summ: Double = 0.0
    var selectedTag: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTagBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener { dismiss() }

        allFilter.tag = selectedTag
        binding.title.text = selectedTag
        setupTime()
        refresh()
        setupTransactionList()

        return binding.root
    }

    private fun refresh() = viewModel.getAllData()

    private fun setupTransactionList() {
        transactionAdapter = TransactionAdapter { }
        viewModel.transactions.observe(viewLifecycleOwner, ::submitList)
        binding.rvTag.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = transactionAdapter
        }
    }

    private fun submitList(it: java.util.ArrayList<Transaction>) {
        transactionList = it
        transactionAdapter.submitList(transactionList.doTagFilter(allFilter))
        binding.rvTag.post {
            summ = ArrayList(transactionList.doTagFilter(allFilter)).getDays().getAmount()
            binding.summ.text = summ.moneyFormat()
        }
    }

    private fun setupTime() {
        binding.tvFromTime.text = allFilter.timeFilter.fromTime.timeFormat()
        binding.tvFromTime.setOnClickListener {
            requireActivity().getTime {
                allFilter.timeFilter.fromTime = it
                binding.tvFromTime.text = allFilter.timeFilter.fromTime.timeFormat()
                refresh()
            }
        }
        binding.tvToTime.text = allFilter.timeFilter.toTime.timeFormat()
        binding.tvToTime.setOnClickListener {
            requireActivity().getTime {
                allFilter.timeFilter.toTime = it
                binding.tvToTime.text = allFilter.timeFilter.toTime.timeFormat()
                refresh()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }

}