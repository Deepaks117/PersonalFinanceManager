package com.example.personalfinancemanager

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    // Change this to return Flow
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    suspend fun getTotalIncome(): Double? {
        return transactionDao.getTotalIncome()
    }

    suspend fun getTotalExpenses(): Double? {
        return transactionDao.getTotalExpenses()
    }
}
