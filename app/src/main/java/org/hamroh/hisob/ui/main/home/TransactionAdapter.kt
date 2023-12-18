package org.hamroh.hisob.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.hisob.R
import org.hamroh.hisob.data.Transaction
import org.hamroh.hisob.databinding.ItemTransactionBinding
import org.hamroh.hisob.utils.moneyFormat
import org.hamroh.hisob.utils.timeFormat

class TransactionAdapter(private var onItemClick: ((Transaction) -> Unit)? = null) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    var items: ArrayList<Transaction> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private var context: Context, private val binding: ItemTransactionBinding, onItemClick: ((Transaction) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var transaction: Transaction

        init {
            itemView.setOnClickListener { onItemClick?.invoke(transaction) }
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(newTransaction: Transaction) {
            this.transaction = newTransaction

            binding.tvAmount.text = transaction.amount.moneyFormat()
            binding.tvTime.text = transaction.time.timeFormat("HH:mm")
            binding.tvNote.text = transaction.note

            setupType()
            setupAmountColor()
            setupAmountPlus()

        }

        @SuppressLint("SetTextI18n")
        private fun setupAmountPlus() {
            val sign = when (transaction.type) {
                0 -> "-"
                1 -> "+"
                2 -> "+"
                3 -> "-"
                4 -> "-"
                else -> "+"
            }
            binding.tvAmount.text = "$sign${binding.tvAmount.text}"
        }

        private fun setupAmountColor() {
            when (transaction.type) {
                0 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense))
                1 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income))
                2 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.borrow))
                3 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.borrow))
                4 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.lending))
                5 -> binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.lending))
                else -> {}
            }
        }

        private fun setupType() {
            when (transaction.type) {
                0 -> binding.ivType.setImageResource(R.drawable.expense)
                1 -> binding.ivType.setImageResource(R.drawable.income)
                2 -> binding.ivType.setImageResource(R.drawable.borrow)
                3 -> binding.ivType.setImageResource(R.drawable.borrow_back)
                4 -> binding.ivType.setImageResource(R.drawable.lend)
                5 -> binding.ivType.setImageResource(R.drawable.lend_back)
                else -> {}
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.context, ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
}