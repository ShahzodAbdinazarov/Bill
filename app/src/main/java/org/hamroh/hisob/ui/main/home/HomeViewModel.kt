package org.hamroh.hisob.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.hamroh.hisob.data.AllFilter
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.data.transaction.TransactionRepository
import org.hamroh.hisob.infra.utils.getDays
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TransactionRepository) : ViewModel() {

    val days = MutableLiveData<ArrayList<DayModel>>()

    fun getAllData(allFilter: AllFilter) {
        viewModelScope.launch {
            val transactions = arrayListOf<Transaction>()
            val list = ArrayList(repository.readAllData())
            for (i in 0 until list.size) {
                if (list[i].time > allFilter.timeFilter.fromTime && list[i].time < allFilter.timeFilter.toTime) {
                    when (list[i].type) {
                        0 -> if (allFilter.typeFilter.expense) transactions.add(list[i])
                        1 -> if (allFilter.typeFilter.income) transactions.add(list[i])
                        2 -> if (allFilter.typeFilter.borrow) transactions.add(list[i])
                        3 -> if (allFilter.typeFilter.borrowBack) transactions.add(list[i])
                        4 -> if (allFilter.typeFilter.lending) transactions.add(list[i])
                        else -> if (allFilter.typeFilter.lendingBack) transactions.add(list[i])
                    }
                }
            }
            days.postValue(transactions.getDays())
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.addTransaction(transaction) }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.updateTransaction(transaction) }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.deleteTransaction(transaction) }
    }

}