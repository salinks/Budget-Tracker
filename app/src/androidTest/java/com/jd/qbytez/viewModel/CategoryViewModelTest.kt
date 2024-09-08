package com.jd.qbytez.viewModel


import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jd.qbytez.database.BudgetTrackerDatabase
import com.jd.qbytez.getOrAwaitValue
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.repository.CategoryRepository
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CategoryViewModelTest : TestCase() {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var budgetTrackerDatabase: BudgetTrackerDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        budgetTrackerDatabase = Room.inMemoryDatabaseBuilder(
            context, BudgetTrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        val dataSource = CategoryRepository(budgetTrackerDatabase.categoryDAO())
        viewModel = CategoryViewModel(dataSource)
    }

    @Test
    fun testCategoryViewModel() {
        viewModel.insert(CategoryEntity(0, "Entertainment", "Expense", 596))
        val result = viewModel.allCategories.getOrAwaitValue().find { it.categoryName == "Entertainment"  }
        assertNotNull(result)
    }


}