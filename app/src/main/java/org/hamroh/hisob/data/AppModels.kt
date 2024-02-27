package org.hamroh.hisob.data

import org.hamroh.hisob.data.transaction.Transaction

data class DayModel(
    val id: Int = 0,
    val time: Long = 0L,
    val transactions: ArrayList<Transaction> = arrayListOf(),
    val amount: Double = 0.0,
)

data class TypeFilter(
    var income: Boolean = true,
    var expense: Boolean = true,
    var borrow: Boolean = true,
    var borrowBack: Boolean = true,
    var lending: Boolean = true,
    var lendingBack: Boolean = true,
)

data class TimeFilter(
    var fromTime: Long = 0L,
    var toTime: Long = System.currentTimeMillis() * 2,
)

data class AllFilter(
    var typeFilter: TypeFilter = TypeFilter(),
    var timeFilter: TimeFilter = TimeFilter(),
    var tag: String = "",
)

