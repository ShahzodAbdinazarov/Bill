package org.hamroh.hisob.data

data class DayModel(
    val id: Int = 0,
    val time: Long = 0L,
    val transactions: ArrayList<Transaction> = arrayListOf(),
)

data class Transaction(
    var id: Int = 0,
    var amount: Double = 0.0,
    var time: Long = 0,
    var note: String = "",
    var type: Int = 0,
)

data class Filter(
    var up: Double = 0.0,
    var down: Double = 0.0,
    var borrow: Double = 0.0,
    var borrowBack: Double = 0.0,
    var lend: Double = 0.0,
    var lendBack: Double = 0.0,
)

