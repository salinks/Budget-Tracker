package com.jd.qbytez.application

import android.app.Application
import android.content.Context
import com.jd.qbytez.database.BudgetTrackerDatabase
import com.jd.qbytez.repository.CategoryRepository
import com.jd.qbytez.repository.TransactionsRepository
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack

class BudgetTrackerApplication : Application() {
     private val database by lazy { BudgetTrackerDatabase.getDatabase(this) }
     val categoryRepository by lazy { CategoryRepository(database.categoryDAO()) }
     val transactionsRepository by lazy { TransactionsRepository(database.transactionDAO()) }



    companion object {
        lateinit var context: Context


    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }



}