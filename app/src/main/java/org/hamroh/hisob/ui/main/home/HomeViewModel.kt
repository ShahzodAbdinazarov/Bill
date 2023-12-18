package org.hamroh.hisob.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.hamroh.hisob.data.DayModel
import org.hamroh.hisob.data.Transaction
import org.hamroh.hisob.utils.getDays

class HomeViewModel : ViewModel() {

    val days = MutableLiveData<ArrayList<DayModel>>()

    fun getDays(transactions: ArrayList<Transaction>) = days.postValue(transactions.getDays())

}