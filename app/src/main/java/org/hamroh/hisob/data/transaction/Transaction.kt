package org.hamroh.hisob.data.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var amount: Double = 0.0,
    var time: Long = 0,
    var note: String = "",
    var type: Int = 0,
)
