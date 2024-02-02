package org.hamroh.hisob.data.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(task: Transaction)

    @Update
    suspend fun updateTransaction(task: Transaction)

    @Delete
    suspend fun deleteTransaction(task: Transaction)

    @Query("SELECT * FROM transaction_table ORDER BY time DESC")
    suspend fun readAllData(): List<Transaction>

}