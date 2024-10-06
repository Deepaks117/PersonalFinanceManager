package com.example.personalfinancemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val balance: MutableLiveData<Double> = MutableLiveData()

    fun addTransaction(amount: Double, description: String, isExpense: Boolean) {
        val transaction = Transaction(amount = amount, description = description, isExpense = isExpense)
        viewModelScope.launch {
            repository.insert(transaction)
            loadTransactions() // Reload transactions after adding
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.update(transaction)
            loadTransactions() // Reload transactions after updating
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
            loadTransactions() // Reload transactions after deleting
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactions.value = repository.getAllTransactions()
            // Update balance if necessary
            val totalIncome = repository.getTotalIncome() ?: 0.0
            val totalExpenses = repository.getTotalExpenses() ?: 0.0
            balance.value = totalIncome - totalExpenses
        }
    }
}
