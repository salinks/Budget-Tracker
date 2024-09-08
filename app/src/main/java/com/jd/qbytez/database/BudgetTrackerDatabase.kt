package com.jd.qbytez.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jd.qbytez.database.dao.CategoryDAO
import com.jd.qbytez.database.dao.TransactionDAO
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.models.TransactionsEntity


@Database(entities = [CategoryEntity::class, TransactionsEntity::class], version = 1, exportSchema = false)
public abstract class BudgetTrackerDatabase : RoomDatabase() {


    abstract fun categoryDAO():CategoryDAO
    abstract fun transactionDAO(): TransactionDAO

    companion object {

        @Volatile
        private var INSTANCE: BudgetTrackerDatabase? = null

        fun getDatabase(context: Context): BudgetTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetTrackerDatabase::class.java,
                    "budget_tracker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}