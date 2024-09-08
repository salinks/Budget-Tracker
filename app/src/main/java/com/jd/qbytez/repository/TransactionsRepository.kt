package com.jd.qbytez.repository

import androidx.annotation.WorkerThread
import com.jd.qbytez.database.dao.BTChartData
import com.jd.qbytez.database.dao.TransactionDAO
import com.jd.qbytez.database.dao.TransactionFilterDateData
import com.jd.qbytez.models.TransactionsEntity
import kotlinx.coroutines.flow.Flow

class TransactionsRepository(private val transactionDao: TransactionDAO) {


    val allTransactions:Flow<List<TransactionsEntity>> = transactionDao.getAllTransactions()
    val recentTransactions:Flow<List<TransactionsEntity>> = transactionDao.getRecentTransactions()
    val mChartData:Flow<List<BTChartData>> = transactionDao.getChartData()
    val monthFilter :Flow<List<TransactionFilterDateData>> = transactionDao.getFilterMonth()

    @WorkerThread
    suspend fun insert(transaction: TransactionsEntity){
        transactionDao.insert(transaction)
    }
    @WorkerThread
    suspend fun update(transaction: TransactionsEntity){
        transactionDao.update(transaction)
    }
    @WorkerThread
    suspend fun delete(transaction: TransactionsEntity){
        transactionDao.delete(transaction)
    }

    fun getFilteredTransactions(item:String):Flow<List<TransactionsEntity>> =transactionDao.getTransactionsByType(item)


}