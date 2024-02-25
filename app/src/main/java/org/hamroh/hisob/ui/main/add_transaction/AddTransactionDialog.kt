package org.hamroh.hisob.ui.main.add_transaction

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.hamroh.hisob.R
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.databinding.FragmentAddTransactionBinding
import org.hamroh.hisob.infra.utils.etMoneyFormat
import org.hamroh.hisob.infra.utils.getDouble
import org.hamroh.hisob.infra.utils.getTime
import org.hamroh.hisob.infra.utils.showSoftKeyboard
import org.hamroh.hisob.infra.utils.timeFormat

@AndroidEntryPoint
class AddTransactionDialog : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentAddTransactionBinding? = null
    private var selectTime: Long = System.currentTimeMillis()
    var onInsert: ((Transaction) -> Unit)? = null
    private var type: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        binding.ibClose.setOnClickListener { dismiss() }

        setTime(System.currentTimeMillis())
        binding.tvTime.setOnClickListener { requireActivity().getTime { setTime(it) } }

        binding.bnType.setOnClickListener { clearType() }

        setTypes()
        binding.etAmount.etMoneyFormat()

        binding.ibSave.setOnClickListener { saveTransaction() }
        binding.bnAmount.setOnClickListener { requireActivity().showSoftKeyboard(binding.etAmount) }

        return binding.root
    }

    private fun setTypes() {
        binding.ibExpense.setOnClickListener { setupType(0, R.drawable.ic_expense, R.color.expense, R.string.expense, R.color.white) }
        binding.ibIncome.setOnClickListener { setupType(1, R.drawable.income, R.color.income, R.string.income, R.color.black) }
        binding.ibBorrow.setOnClickListener { setupType(2, R.drawable.ic_borrow, R.color.borrow, R.string.borrow, R.color.black) }
        binding.ibBorrowBack.setOnClickListener { setupType(3, R.drawable.ic_borrow_back, R.color.borrow, R.string.borrow_back, R.color.black) }
        binding.ibLending.setOnClickListener { setupType(4, R.drawable.lend, R.color.lending, R.string.lend, R.color.white) }
        binding.ibLendingBack.setOnClickListener { setupType(5, R.drawable.lend_back, R.color.lending, R.string.lend_back, R.color.white) }
    }

    private fun setTime(time: Long) {
        selectTime = time
        binding.tvTime.text = selectTime.timeFormat()
    }

    private fun saveTransaction() {
        if (validation()) {
            onInsert?.invoke(
                Transaction(
                    amount = binding.etAmount.text.toString().getDouble(),
                    time = selectTime,
                    note = binding.etNote.text.toString(),
                    type = type
                )
            )
            dismiss()
        }
    }

    private fun validation(): Boolean {
        return if (type == -1) {
            binding.tvTypeError.visibility = View.VISIBLE
            false
        } else if (binding.etAmount.text.toString().isEmpty()) {
            binding.etAmount.error = "Mablag‘ni kiriting!"
            binding.etAmount.requestFocus()
            false
        } else true
    }

    private fun clearType() {
        type = -1
        binding.llFilter.visibility = View.VISIBLE
        binding.mvType.visibility = View.GONE
    }

    private fun setupType(newType: Int, image: Int, color: Int, name: Int, tint: Int) {
        type = newType
        binding.tvTypeError.visibility = View.GONE
        binding.llFilter.visibility = View.GONE
        binding.mvType.visibility = View.VISIBLE
        binding.ivType.setImageResource(image)
        binding.mvType.setCardBackgroundColor(ContextCompat.getColor(requireContext(), color))
        binding.tvType.setTextColor(ContextCompat.getColor(requireContext(), tint))
        binding.tvType.text = requireActivity().getString(name)
        binding.ivType.setColorFilter(ContextCompat.getColor(requireContext(), tint), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }

}
