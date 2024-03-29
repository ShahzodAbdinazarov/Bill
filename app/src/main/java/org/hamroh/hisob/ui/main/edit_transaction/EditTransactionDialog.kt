package org.hamroh.hisob.ui.main.edit_transaction

import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.hamroh.hisob.R
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.databinding.FragmentEditTransactionBinding
import org.hamroh.hisob.infra.utils.getDouble
import org.hamroh.hisob.infra.utils.getTime
import org.hamroh.hisob.infra.utils.showSoftKeyboard
import org.hamroh.hisob.infra.utils.timeFormat

@AndroidEntryPoint
class EditTransactionDialog : BottomSheetDialogFragment() {

    private var selectTime: Long = System.currentTimeMillis()
    private var type: Int = -1
    var transaction = Transaction()
    private var _binding: FragmentEditTransactionBinding? = null
    private val binding get() = _binding!!
    var onUpdate: ((Transaction) -> Unit)? = null
    var onDelete: ((Transaction) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditTransactionBinding.inflate(inflater, container, false)
        binding.ibClose.setOnClickListener { dismiss() }

        setTime(System.currentTimeMillis())
        binding.tvTime.setOnClickListener { requireActivity().getTime { setTime(it) } }

        binding.bnType.setOnClickListener { clearType() }

        binding.ibExpense.setOnClickListener { setType(0) }
        binding.ibIncome.setOnClickListener { setType(1) }
        binding.ibBorrow.setOnClickListener { setType(2) }
        binding.ibBorrowBack.setOnClickListener { setType(3) }
        binding.ibLending.setOnClickListener { setType(4) }
        binding.ibLendingBack.setOnClickListener { setType(5) }

        binding.ibSave.setOnClickListener { saveTransaction() }
        binding.ibDelete.setOnClickListener { deleteTransaction() }
        binding.bnAmount.setOnClickListener { requireActivity().showSoftKeyboard(binding.etAmount) }

        setupTransaction()

        return binding.root
    }

    private fun setupTransaction() {
        setTime(transaction.time)
        setType(transaction.type)
        setAmount(transaction.amount)
        setNote(transaction.note)
    }

    private fun setNote(note: String) = binding.etNote.setText(note)

    private fun setAmount(amount: Double) = binding.etAmount.setText(amount.toString())

    private fun setType(i: Int) {
        when (i) {
            0 -> setupType(0, R.drawable.ic_expense, R.color.expense, R.string.expense, R.color.white)
            1 -> setupType(1, R.drawable.income, R.color.income, R.string.income, R.color.black)
            2 -> setupType(2, R.drawable.ic_borrow, R.color.borrow, R.string.borrow, R.color.black)
            3 -> setupType(3, R.drawable.ic_borrow_back, R.color.borrow, R.string.borrow_back, R.color.black)
            4 -> setupType(4, R.drawable.lend, R.color.lending, R.string.lend, R.color.white)
            5 -> setupType(5, R.drawable.lend_back, R.color.lending, R.string.lend_back, R.color.white)
        }
    }

    private fun setTime(time: Long) {
        selectTime = time
        binding.tvTime.text = selectTime.timeFormat()
    }

    private fun saveTransaction() {
        if (validation()) {
            onUpdate?.invoke(Transaction(transaction.id, binding.etAmount.text.toString().getDouble(), selectTime, note = binding.etNote.text.toString(), type))
            dismiss()
        }
    }

    private fun deleteTransaction() {
        val dialog = Dialog(requireContext(), R.style.DeleteDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_delete_transaction)

        val tvNo: TextView = dialog.findViewById(R.id.tv_no)
        val tvYes: TextView = dialog.findViewById(R.id.tv_yes)

        tvNo.setOnClickListener { dialog.dismiss() }
        tvYes.setOnClickListener {
            onDelete?.invoke(transaction)
            dialog.dismiss()
            dismiss()
        }

        dialog.show()
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