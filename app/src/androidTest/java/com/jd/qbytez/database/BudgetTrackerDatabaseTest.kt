package com.jd.qbytez.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jd.qbytez.database.dao.CategoryDAO
import com.jd.qbytez.database.dao.TransactionDAO
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.models.TransactionsEntity
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BudgetTrackerDatabaseTest : TestCase() {

    private lateinit var db: BudgetTrackerDatabase
    private lateinit var catDao: CategoryDAO
    private lateinit var transDao: TransactionDAO

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BudgetTrackerDatabase::class.java).build()
        catDao = db.categoryDAO()
        transDao = db.transactionDAO()
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    @Test
    fun insertAndReadCategory() = runBlocking() {
        val model = CategoryEntity(0, "Entertainment", "Expense", 596)
        catDao.addCategory(model)
        val categories = catDao.getRecentCategories()
        assertEquals(1, categories.size)
        assertEquals("Entertainment", categories[0].categoryName)
    }

    @Test
    fun insertAndReadTransaction() = runBlocking() {
        val model = TransactionsEntity(
            "Movie",
            100.0,
            "Expense",
            "Entertainment",
            596,
            "2024-09-05",
            "Description",
            "2024-09-07 18:45:32",
            0
        )
        transDao.insert(model)
        val trans = transDao.getRecentTrans()
        assertEquals(1, trans.size)
        assertEquals("Movie", trans[0].title)
    }

}