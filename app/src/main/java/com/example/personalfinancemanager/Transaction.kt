package com.example.personalfinancemanager

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "transactions") // Ensure the table name matches your queries
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Ensure id is defined as primary key
    val amount: Double,
    val description: String,
    val isExpense: Boolean,
    val date: Long, // Ensure this is of type Long if you're storing a timestamp
    val category: String // Ensure this field exists if you're using it
)

