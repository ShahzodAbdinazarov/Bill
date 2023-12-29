package org.hamroh.hisob.ui.main.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.hisob.R
import org.hamroh.hisob.databinding.FragmentProfileBinding
import org.hamroh.hisob.utils.SharedPrefs
import org.hamroh.hisob.utils.closeKeyboard


class ProfileDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    var onClick: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.etName.setText(SharedPrefs(requireContext()).name)
        binding.etName.requestFocus()
        binding.etName.setSelection(binding.etName.text.toString().length)

        binding.ibDone.setOnClickListener {
            if (validation()) {
                SharedPrefs(requireContext()).setName(binding.etName.text.toString().trim())
                requireActivity().closeKeyboard(binding.etName)
                dismiss()
                onClick?.invoke(true)
            } else binding.etName.error = getString(R.string.enter_your_name)
        }

        binding.ibClose.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun validation(): Boolean = binding.etName.text.toString().trim().isNotEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

}