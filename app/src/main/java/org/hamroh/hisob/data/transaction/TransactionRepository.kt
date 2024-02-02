package org.hamroh.hisob.data.transaction

import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {

    suspend fun addTransaction(transaction: Transaction) = transactionDao.addTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) = transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.deleteTransaction(transaction)

    suspend fun readAllData(): List<Transaction> = transactionDao.readAllData()
}