package org.hamroh.hisob.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.hisob.R
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.Transaction
import org.hamroh.hisob.databinding.ItemDayBinding
import org.hamroh.hisob.utils.getDate
import org.hamroh.hisob.utils.moneyFormat
import kotlin.properties.Delegates

class DayAdapter(private var onItemClick: ((Transaction) -> Unit)? = null) : ListAdapter<DayModel, DayAdapter.ViewHolder>(DIFF_UTIL()) {

    private class DIFF_UTIL : DiffUtil.ItemCallback<DayModel>() {
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean = oldItem.time == newItem.time
        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean = oldItem == newItem
    }

    class ViewHolder(private val binding: ItemDayBinding, private val onItemClick: ((Transaction) -> Unit)?, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        private var day by Delegates.notNull<DayModel>()

        fun bind(newDay: DayModel) {
            this.day = newDay

            binding.tvName.text = day.time.getDate("dd-MMMM")
            binding.tvAmount.text = if (day.amount > 0) "+${day.amount.moneyFormat()}" else day.amount.moneyFormat()
            if (day.amount > 0) binding.mvDay.strokeColor = ContextCompat.getColor(context, R.color.income)
            else binding.mvDay.strokeColor = ContextCompat.getColor(context, R.color.expense)

            setupDays(day.transactions)

        }

        private fun setupDays(transactions: ArrayList<Transaction>) {
            val transactionAdapter = TransactionAdapter { onItemClick?.invoke(it) }

            binding.rvTransaction.apply {
                layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                adapter = transactionAdapter.apply { items = transactions }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick, parent.context)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

