package com.example.personalfinancemanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var balanceTextView: TextView
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Room database and DAO
        val transactionDao = AppDatabase.getDatabase(applicationContext).transactionDao()
        val repository = TransactionRepository(transactionDao)
        val viewModelFactory = TransactionViewModelFactory(repository)

        // Initialize ViewModel using ViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]

        balanceTextView = findViewById(R.id.balanceTextView)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)

        // Initialize buttons
        val addIncomeButton: Button = findViewById(R.id.addIncomeButton)
        val addExpenseButton: Button = findViewById(R.id.addExpenseButton)

        // Set click listeners for buttons
        addIncomeButton.setOnClickListener { showAddTransactionDialog(isExpense = false) }
        addExpenseButton.setOnClickListener { showAddTransactionDialog(isExpense = true) }

        // Initialize the Floating Action Button
        val addTransactionFab: FloatingActionButton = findViewById(R.id.addTransactionFab)

        // Set click listener for FAB
        addTransactionFab.setOnClickListener {
            showAddTransactionDialog(isExpense = false) // Open the dialog for adding income
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        viewModel = TransactionViewModel(repository)
    }


    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter { transaction ->
            showEditTransactionDialog(transaction) // Pass the transaction to edit
        }
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

    private fun showEditTransactionDialog(transaction: Transaction) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val amountEditText = dialogView.findViewById<TextInputEditText>(R.id.amountEditText)
        val descriptionEditText = dialogView.findViewById<TextInputEditText>(R.id.descriptionEditText)

        // Pre-fill the dialog with current transaction data
        amountEditText.setText(transaction.amount.toString())
        descriptionEditText.setText(transaction.description)

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Transaction")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
                val description = descriptionEditText.text.toString()
                viewModel.updateTransaction(transaction.copy(amount = amount, description = description))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
