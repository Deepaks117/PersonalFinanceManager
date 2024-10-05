package com.example.personalfinancemanager

import android.content.Context
import androidx.room.*

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): List<Transaction>

    @Insert
    fun insertTransaction(transaction: Transaction)

    @Query("SELECT SUM(CASE WHEN isExpense = 0 THEN amount ELSE -amount END) FROM transactions")
    fun getTotalBalance(): Double
}