package org.hamroh.hisob.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.Transaction
import org.hamroh.hisob.databinding.ItemDayBinding
import org.hamroh.hisob.utils.getDate
import kotlin.properties.Delegates

class DayAdapter(private var onItemClick: ((DayModel) -> Unit)? = null) : ListAdapter<DayModel, DayAdapter.ViewHolder>(DIFF_UTIL()) {

    private class DIFF_UTIL : DiffUtil.ItemCallback<DayModel>() {
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean = oldItem.time == newItem.time
        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean = oldItem == newItem
    }

    class ViewHolder(private val binding: ItemDayBinding, onItemClick: ((DayModel) -> Unit)?) : RecyclerView.ViewHolder(binding.root) {

        private var day by Delegates.notNull<DayModel>()

        init {
            itemView.setOnClickListener { onItemClick?.invoke(day) }
        }

        fun bind(newDay: DayModel) {
            this.day = newDay

            binding.tvName.text = day.time.getDate("dd-MMMM")

            setupDays(day.transactions)

        }

        private fun setupDays(transactions: ArrayList<Transaction>) {
            val transactionAdapter = TransactionAdapter {}

            binding.rvTransaction.apply {
                layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)//.apply { reverseLayout = true }
                adapter = transactionAdapter.apply { items = transactions }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

