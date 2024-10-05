package com.example.personalfinancemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var balanceTextView: TextView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        balanceTextView = findViewById(R.id.balanceTextView)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)

        val addIncomeButton: Button = findViewById(R.id.addIncomeButton)
        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)

        addIncomeButton.setOnClickListener { showAddTransactionDialog(false) }
        addExpenseButton.setOnClickListener { showAddTransactionDialog(true) }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter()
        transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.balance.observe(this) { balance ->
            balanceTextView.text = String.format("Total Balance: $%.2f", balance)
        }

        viewModel.transactions.observe(this) { transactions ->
            transactionAdapter.setTransactions(transactions)
        }
    }

    private fun showAddTransactionDialog(isExpense: Boolean) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val amountEditText = dialogView.findViewById<TextInputEditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<TextInputEditText>(R.id.descriptionEditText)

        MaterialAlertDialogBuilder(this)
            .setTitle(if (isExpense) "Add Expense" else "Add Income")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
                val description = descriptionEditText.text.toString()
                viewModel.addTransaction(amount, description, isExpense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}