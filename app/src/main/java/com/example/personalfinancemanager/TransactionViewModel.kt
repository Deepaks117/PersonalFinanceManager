package com.example.personalfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    // Observe all transactions from the repository
    val transactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    // Calculate balance based on transactions
    val balance: LiveData<Double> = repository.allTransactions.map { transactions ->
        transactions.sumOf { if (it.isExpense) -it.amount else it.amount }
    }.asLiveData()

    // Method to add a transaction
    fun addTransaction(amount: Double, description: String, isExpense: Boolean, category: String) {
        val transaction = Transaction(amount = amount, description = description, isExpense = isExpense, category = category, date = System.currentTimeMillis())
        viewModelScope.launch {
            repository.insert(transaction)
            // No need to call loadTransactions() here; LiveData will auto-update
        }
    }

    // Method to update a transaction
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.update(transaction)
            // No need to call loadTransactions() here; LiveData will auto-update
        }
    }

    // Method to delete a transaction
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
            // No need to call loadTransactions() here; LiveData will auto-update
        }
    }
}
