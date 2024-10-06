package com.example.personalfinancemanager

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val transactionDao: TransactionDao) {

    suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.insert(transaction)
        }
    }

    suspend fun update(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.update(transaction)
        }
    }

    suspend fun delete(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.delete(transaction)
        }
    }

    suspend fun getAllTransactions(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            transactionDao.getAllTransactions()
        }
    }

    suspend fun getTotalIncome(): Double? {
        return withContext(Dispatchers.IO) {
            transactionDao.getTotalIncome()
        }
    }

    suspend fun getTotalExpenses(): Double? {
        return withContext(Dispatchers.IO) {
            transactionDao.getTotalExpenses()
        }
    }
}
