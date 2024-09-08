package com.jd.qbytez.viewModel

import androidx.lifecycle.*
import com.jd.qbytez.database.dao.BTChartData
import com.jd.qbytez.database.dao.TransactionFilterDateData
import com.jd.qbytez.models.TransactionsEntity
import com.jd.qbytez.repository.TransactionsRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val transactionRepository: TransactionsRepository):ViewModel() {

    val allTransactions:LiveData<List<TransactionsEntity>> =transactionRepository.allTransactions.asLiveData()
    val recentTransactions:LiveData<List<TransactionsEntity>> =transactionRepository.recentTransactions.asLiveData()
    val mChartData:LiveData<List<BTChartData>> =transactionRepository.mChartData.asLiveData()
    val monthFilter:LiveData<List<TransactionFilterDateData>> =transactionRepository.monthFilter.asLiveData()

    fun insert(transaction: TransactionsEntity)=viewModelScope.launch {
        transactionRepository.insert(transaction)
    }
    fun update(transaction: TransactionsEntity)=viewModelScope.launch {
        transactionRepository.update(transaction)
    }
    fun delete(transaction: TransactionsEntity)=viewModelScope.launch {
        transactionRepository.delete(transaction)
    }
    fun getFilteredTransactions(item:String):LiveData<List<TransactionsEntity>> =transactionRepository.getFilteredTransactions(item).asLiveData()



}
class TransactionViewModelFactory(private val transactionRepository: TransactionsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}