package org.hamroh.hisob.ui.about.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import org.hamroh.hisob.R
import org.hamroh.hisob.databinding.FragmentAboutMainBinding
import java.util.Locale

@SuppressLint("InflateParams")
class MainFragment : Fragment() {
    private val data: MutableList<String> = ArrayList()
    private var listSavol: ListView? = null
    private var edtSearch: TextInputEditText? = null
    private var searchData: MutableList<String>? = null
    private var _binding: FragmentAboutMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAboutMainBinding.inflate(inflater, container, false)

        val root = inflater.inflate(R.layout.fragment_about_main, null)
        val close = root.findViewById<ImageView>(R.id.close)
        val search = root.findViewById<ImageView>(R.id.search)
        listSavol = root.findViewById(R.id.listSavol)
        edtSearch = root.findViewById(R.id.edtSearch)
        close.setOnClickListener { requireActivity().finish() }
        search.setOnClickListener {
            binding.edtSearch.requestFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (binding.edtSearch.text?.isNotEmpty() == true) {
                imm.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
            } else {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }
        refresh()
        searchSavol()
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchSavol()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.listSavol.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            val fragment: Fragment = SavolFragment()
            val bundle = Bundle()
            bundle.putString("savol", searchData!![position])
            fragment.arguments = bundle
            if (activity != null) {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.aboutContainer, fragment).commit()
            }
        }
        return root
    }

    private fun refresh() {
        data.add("Bu o'zi qanday dastur?")
        data.add("Dasturdan qanday foydalaniladi?")
        data.add("Kirim qanday kiritiladi?")
        data.add("Kiritilgan hisob qanday o'chiriladi?")
        data.add("Hisob qanday o'zgartiriladi?")
        data.add("Izoh qayerdan ko'riladi?")
        data.add("Vaqt bo'yicha qanday filterlanadi?")
        data.add("Hisob turi bo'yicha qanday filterlanadi?")
        data.add("Men qanday homiylik qilishim mumkin?")
        if (activity != null) {
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, data)
            listSavol!!.adapter = adapter
        }
    }

    private fun searchSavol() {
        searchData = ArrayList()
        val text = if (edtSearch!!.text != null) edtSearch!!.text.toString().lowercase(Locale.getDefault()) else ""
        for (i in data.indices) {
            val search = data[i].lowercase(Locale.getDefault())
            if (search.contains(text)) {
                (searchData as ArrayList<String>).add(data[i])
            }
        }
        if (activity != null) {
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, searchData as ArrayList<String>)
            listSavol!!.adapter = adapter
        }
    }
}
