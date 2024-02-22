package org.hamroh.hisob.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.hamroh.hisob.data.transaction.Transaction
import org.hamroh.hisob.data.transaction.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TransactionRepository) : ViewModel() {

    val transactions = MutableLiveData<ArrayList<Transaction>>()

    fun getAllData() {
        viewModelScope.launch { transactions.postValue(ArrayList(repository.readAllData())) }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.addTransaction(transaction);getAllData() }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.updateTransaction(transaction);getAllData() }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.deleteTransaction(transaction);getAllData() }
    }

}