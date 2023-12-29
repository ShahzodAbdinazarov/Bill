package org.hamroh.hisob.ui.main.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.hisob.R
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.databinding.FragmentFilterBinding
import org.hamroh.hisob.utils.getTime
import org.hamroh.hisob.utils.timeFormat


class FilterDialog : BottomSheetDialogFragment() {

    var allFilter = AllFilter()
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    var onClick: ((AllFilter) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        binding.ibClose.setOnClickListener { dismiss() }

        setupTime()
        setupType()
        setTypes()
        binding.ibSave.setOnClickListener {
            onClick?.invoke(allFilter)
            dismiss()
        }

        return binding.root
    }

    private fun setupTime() {
        binding.tvFromTime.text = allFilter.timeFilter.fromTime.timeFormat()
        binding.tvFromTime.setOnClickListener {
            requireActivity().getTime {
                allFilter.timeFilter.fromTime = it
                binding.tvFromTime.text = allFilter.timeFilter.fromTime.timeFormat()
            }
        }
        binding.tvToTime.text = allFilter.timeFilter.toTime.timeFormat()
        binding.tvToTime.setOnClickListener {
            requireActivity().getTime {
                allFilter.timeFilter.toTime = it
                binding.tvToTime.text = allFilter.timeFilter.toTime.timeFormat()
            }
        }
    }

    private fun setupType() {
        binding.ibIncome.setOnClickListener {
            allFilter.typeFilter.income = !allFilter.typeFilter.income
            setTypes()
        }
        binding.ibExpense.setOnClickListener {
            allFilter.typeFilter.expence = !allFilter.typeFilter.expence
            setTypes()
        }
        binding.ibBorrow.setOnClickListener {
            allFilter.typeFilter.borrow = !allFilter.typeFilter.borrow
            setTypes()
        }
        binding.ibBorrowBack.setOnClickListener {
            allFilter.typeFilter.borrowBack = !allFilter.typeFilter.borrowBack
            setTypes()
        }
        binding.ibLending.setOnClickListener {
            allFilter.typeFilter.lending = !allFilter.typeFilter.lending
            setTypes()
        }
        binding.ibLendingBack.setOnClickListener {
            allFilter.typeFilter.lendingBack = !allFilter.typeFilter.lendingBack
            setTypes()
        }

    }

    private fun setTypes() {
        if (!allFilter.typeFilter.income) binding.mvIncome.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvIncome.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
        if (!allFilter.typeFilter.expence) binding.mvExpense.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvExpense.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
        if (!allFilter.typeFilter.borrow) binding.mvBorrow.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvBorrow.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
        if (!allFilter.typeFilter.borrowBack) binding.mvBorrowBack.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvBorrowBack.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
        if (!allFilter.typeFilter.lending) binding.mvLending.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvLending.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
        if (!allFilter.typeFilter.lendingBack) binding.mvLendingBack.strokeColor = ContextCompat.getColor(requireContext(), R.color.max)
        else binding.mvLendingBack.strokeColor = ContextCompat.getColor(requireContext(), R.color.reverse)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }

}