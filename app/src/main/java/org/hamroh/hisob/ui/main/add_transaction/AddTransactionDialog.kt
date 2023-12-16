package org.hamroh.hisob.ui.main.add_transaction

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.History
import org.hamroh.hisob.databinding.FragmentAddTransactionBinding
import org.hamroh.hisob.utils.getTime
import org.hamroh.hisob.utils.timeFormat

class AddTransactionDialog : BottomSheetDialogFragment() {

    private var selectTime: Long = System.currentTimeMillis()
    private var type: Int = -1
    var money = 0.0
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    var onClick: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        setTime(System.currentTimeMillis())
        binding.tvTime.setOnClickListener { requireActivity().getTime { setTime(it) } }

        binding.bnType.setOnClickListener { clearType() }

        binding.ibExpense.setOnClickListener { setupType(0, R.drawable.expense, R.color.expense, R.string.expense, R.color.white) }
        binding.ibIncome.setOnClickListener { setupType(1, R.drawable.income, R.color.income, R.string.income, R.color.black) }
        binding.ibBorrow.setOnClickListener { setupType(2, R.drawable.borrow, R.color.borrow, R.string.borrow, R.color.black) }
        binding.ibBorrowBack.setOnClickListener { setupType(3, R.drawable.borrow_back, R.color.borrow, R.string.borrow_back, R.color.black) }
        binding.ibLending.setOnClickListener { setupType(4, R.drawable.lend, R.color.lending, R.string.lend, R.color.white) }
        binding.ibLendingBack.setOnClickListener { setupType(5, R.drawable.lend_back, R.color.lending, R.string.lend_back, R.color.white) }

        binding.tvSave.setOnClickListener { saveTransaction() }

        return binding.root
    }

    private fun setTime(time: Long) {
        selectTime = time
        binding.tvTime.text = selectTime.timeFormat()
    }

    private fun saveTransaction() {
        val db = DBHelper(requireContext())
        if (validation()) {
            db.add(History(1, binding.etAmount.text.toString().toDouble(), selectTime, desc = binding.etDesc.text.toString(), type))
            onClick?.invoke(true)
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
        } else if (binding.etAmount.text.toString().toDouble() > money && (type == 0 || type == 3 || type == 4)) {
            binding.etAmount.error = "Yetarli mablag‘ mavjud emas!"
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