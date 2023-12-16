package org.hamroh.hisob.data

data class History(
    var id: Int = 0,
    var amount: Double = 0.0,
    var time: Long = 0,
    var desc: String = "",
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

data class Savol(val id: Int = 0, var savol: String = "", var javob: String = "")