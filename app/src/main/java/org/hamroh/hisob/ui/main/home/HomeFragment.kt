package org.hamroh.hisob.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.hamroh.hisob.R
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.databinding.FragmentHomeBinding
import org.hamroh.hisob.infra.utils.SharedPrefs
import org.hamroh.hisob.infra.utils.doFilter
import org.hamroh.hisob.infra.utils.getAmount
import org.hamroh.hisob.infra.utils.moneyFormat
import org.hamroh.hisob.ui.main.add_transaction.AddTransactionDialog
import org.hamroh.hisob.ui.main.edit_transaction.EditTransactionDialog
import org.hamroh.hisob.ui.main.filter.FilterDialog
import org.hamroh.hisob.ui.main.profile.ProfileDialog
import org.hamroh.hisob.ui.main.tag.TagFragment


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private var transactionList = arrayListOf<Transaction>()
    private val viewModel: HomeViewModel by viewModels()
    private var dayAdapter: DayAdapter = DayAdapter()
    private var _binding: FragmentHomeBinding? = null
    private var allFilter = AllFilter()
    private var currentAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.shimmer.startShimmer()
        refresh()
        setupTransactionList()
        setupProfile()

        binding.fab.setOnClickListener {
            val addTransaction = AddTransactionDialog()
            addTransaction.onInsert = ::addTransaction
            addTransaction.show(requireActivity().supportFragmentManager, "AddTransactionDialog")
        }

        binding.ibFilter.setOnClickListener {
            val filter = FilterDialog()
            filter.allFilter = allFilter
            filter.onChange = { allFilter = it;refresh() }
            filter.show(requireActivity().supportFragmentManager, "FilterDialog")
        }

        binding.rvTransaction.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isRecyclerAtTop() && dy < 0 && binding.fabScrollTop.isShown) binding.fabScrollTop.hide()
                else if (!isRecyclerAtTop() && dy > 0 && !binding.fabScrollTop.isShown) binding.fabScrollTop.show()
            }
        })

        binding.fabScrollTop.setOnClickListener {
            if (binding.rvTransaction.adapter != null && binding.rvTransaction.adapter!!.itemCount > 0)
                binding.rvTransaction.smoothScrollToPosition(0)
        }

        return binding.root
    }

    private fun isRecyclerAtTop(): Boolean {
        val layoutManager = binding.rvTransaction.layoutManager as StaggeredGridLayoutManager
        val firstVisibleItems = IntArray(layoutManager.spanCount)
        layoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItems)
        for (position in firstVisibleItems) if (position == 1) return true
        return false
    }

    private fun refresh() = viewModel.getAllData()

    private fun setupProfile() {
        setName()
        binding.bnProfile.setOnClickListener {
            val profile = ProfileDialog()
            profile.onSave = { setName() }
            profile.show(requireActivity().supportFragmentManager, "ProfileDialog")
        }
    }

    private fun setName() {
        val name = SharedPrefs(requireContext()).name
        binding.tvName.text = name.ifEmpty { getString(R.string.your_name) }
    }

    private fun setupTransactionList() {
        dayAdapter = DayAdapter(::openEdit, ::openTag)
        viewModel.transactions.observe(viewLifecycleOwner, ::submitList)
        binding.rvTransaction.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = dayAdapter
        }
    }

    private fun openTag(tag: String) {
        val tagFragment = TagFragment()
        tagFragment.selectedTag = tag
        tagFragment.show(requireActivity().supportFragmentManager, "TagFragment")
    }

    private fun openEdit(it: Transaction) {
        val editTransaction = EditTransactionDialog()
        editTransaction.transaction = it
        editTransaction.onDelete = viewModel::deleteTransaction
        editTransaction.onUpdate = viewModel::updateTransaction
        editTransaction.show(requireActivity().supportFragmentManager, "EditTransactionDialog")
    }

    private fun submitList(it: java.util.ArrayList<Transaction>) {
        transactionList = it
        dayAdapter.submitList(transactionList.doFilter(allFilter))
        //Todo: Bu yerda qo'shilgan yoki o'zgargan transactionni indexini aniqlashim kerak va

        binding.rvTransaction.post {
            currentAmount = transactionList.doFilter(allFilter).getAmount()
            binding.current.text = currentAmount.moneyFormat()
            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
            // TODO: bu yerda usha indexga scroll qilishim kerak
        }
    }

    private fun addTransaction(it: Transaction) {
        viewModel.addTransaction(it)
        transactionList.add(it)
        submitList(transactionList)

    }

}
