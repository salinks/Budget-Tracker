package com.jd.qbytez.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.models.TransactionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insert(transaction: TransactionsEntity)

    @Update
    suspend fun update(transaction: TransactionsEntity)

    @Delete
    suspend fun delete(transaction: TransactionsEntity)

    @Query("SELECT * FROM bt_transactions ORDER BY transactionId DESC LIMIT 5")
    fun getRecentTransactions(): Flow<List<TransactionsEntity>>

    @Query("SELECT * FROM bt_transactions ORDER BY transactionId DESC")
    fun getAllTransactions(): Flow<List<TransactionsEntity>>

    @Query("SELECT * FROM bt_transactions WHERE transactionType= :item")
    fun getTransactionsByType(item: String): Flow<List<TransactionsEntity>>

    @Query(
        "SELECT COALESCE(SUM(CASE WHEN transactionType ='Income' THEN amount ELSE 0 END ),0) AS totalIncome,\n" +
                "COALESCE(SUM(CASE WHEN transactionType ='Expense' THEN amount ELSE 0 END ),0) AS totalExpense,\n" +
                " strftime('%m',transactionDate) AS monthNum\n" +
                "FROM bt_transactions  WHERE  strftime('%Y',transactionDate)  =  strftime('%Y',date())  GROUP BY strftime('%Y',transactionDate), strftime('%m',transactionDate)"
    )
    fun getChartData(): Flow<List<BTChartData>>

    @Query(
        "SELECT COALESCE(SUM(CASE WHEN transactionType ='Income' THEN amount ELSE 0 END ),0) AS totalIncome,\n" +
                "COALESCE(SUM(CASE WHEN transactionType ='Expense' THEN amount ELSE 0 END ),0) AS totalExpense,\n" +
                "strftime('%m-%Y',transactionDate) AS monthYearName,\n" +
                "strftime('%m-%Y',transactionDate) AS monthYear,\n" +
                "strftime('%m',transactionDate) AS monthName, \n" +
                "strftime('%Y') AS yearNum\n" +
                "FROM bt_transactions    GROUP BY strftime('%Y',transactionDate), strftime('%m',transactionDate)"
    )
    fun getFilterMonth(): Flow<List<TransactionFilterDateData>>

    @Query("SELECT * FROM bt_transactions ORDER BY transactionId DESC")
    fun getRecentTrans() : List<TransactionsEntity>
}

 class BTChartData {
    var totalIncome: Double = 0.0
    var totalExpense: Double = 0.0
    var monthNum: String? = null
}
class TransactionFilterDateData {
    var totalIncome: Double = 0.0
    var totalExpense: Double = 0.0
    var monthYear: String = ""
    var monthYearName: String = ""
    var monthName: String= ""
    var yearNum: String= ""
}
