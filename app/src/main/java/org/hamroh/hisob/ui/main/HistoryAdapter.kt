package org.hamroh.hisob.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DBHelper
import org.hamroh.hisob.data.History
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter internal constructor(private val activity: Activity, private val data: List<History>) : BaseAdapter() {
    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val db: DBHelper = DBHelper(activity)

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val root = inflater.inflate(R.layout.row, null)
        val rowImage = root.findViewById<ImageView>(R.id.rowImage)
        val txtMoney = root.findViewById<TextView>(R.id.txtMoney)
        val txtTime = root.findViewById<TextView>(R.id.txtTime)
        val txtComment = root.findViewById<TextView>(R.id.txtComment)
        when (data[position].type) {
            0 -> rowImage.setImageResource(R.drawable.down)
            1 -> rowImage.setImageResource(R.drawable.up)
            2 -> rowImage.setImageResource(R.drawable.ic_borrow)
            3 -> rowImage.setImageResource(R.drawable.ic_borrow_back)
            4 -> rowImage.setImageResource(R.drawable.ic_lend)
            5 -> rowImage.setImageResource(R.drawable.ic_lend_back)
            else -> {}
        }
        val number = data[position].money
        val decimalFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val money = decimalFormat.format(number)
        txtMoney.text = money
        txtTime.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date(data[position].time))
        txtComment.text = data[position].info
        txtComment.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        val `is` = booleanArrayOf(true)
        root.setOnClickListener {
            if (`is`[0]) {
                txtComment.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            } else {
                txtComment.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
            }
            `is`[0] = !`is`[0]
        }
        root.setOnLongClickListener {
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(money)
            when (data[position].type) {
                0 -> alert.setIcon(R.drawable.down)
                1 -> alert.setIcon(R.drawable.up)
                2 -> alert.setIcon(R.drawable.ic_borrow)
                3 -> alert.setIcon(R.drawable.ic_borrow_back)
                4 -> alert.setIcon(R.drawable.ic_lend)
                5 -> alert.setIcon(R.drawable.ic_lend_back)
                else -> {}
            }
            alert.setMessage(R.string.delete)
            alert.setPositiveButton(android.R.string.yes) { dialog: DialogInterface, _: Int ->
                db.delete(data[position].id)
                activity.recreate()
                dialog.dismiss()
            }
            alert.setNegativeButton(android.R.string.no) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            alert.show()
            true
        }
        return root
    }
}
