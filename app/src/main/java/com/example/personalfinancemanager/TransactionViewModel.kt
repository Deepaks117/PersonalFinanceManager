package com.example.personalfinancemanager

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val transactionDao = database.transactionDao()

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> = _balance

    init {
        loadTransactions()
        updateBalance()
    }

    private fun loadTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactions.postValue(transactionDao.getAllTransactions())
        }
    }

    private fun updateBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            _balance.postValue(transactionDao.getTotalBalance())
        }
    }

    fun addTransaction(amount: Double, description: String, isExpense: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = Transaction(amount = amount, description = description, isExpense = isExpense)
            transactionDao.insertTransaction(transaction)
            loadTransactions()
            updateBalance()
        }
    }
}