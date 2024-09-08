package com.jd.qbytez.repository

import androidx.lifecycle.LiveData
import com.jd.qbytez.database.BudgetTrackerDatabase
import com.jd.qbytez.database.dao.CategoryDAO
import com.jd.qbytez.models.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryRepository(catDAO: CategoryDAO) {

    private var categoryDAO: CategoryDAO = catDAO

    fun getCategories(): LiveData<List<CategoryEntity>> {
        return categoryDAO.getAllCategories()
    }

    fun insertCategory(category: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.addCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.deleteCategory(category)
        }
    }

    fun updateCategory(category: CategoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDAO.updateCategory(category)
        }
    }






}
