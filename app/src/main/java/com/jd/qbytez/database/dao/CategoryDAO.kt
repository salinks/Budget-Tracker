package com.jd.qbytez.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jd.qbytez.models.CategoryEntity

@Dao
interface CategoryDAO {
    @Insert
    suspend  fun addCategory(category: CategoryEntity) : Long

    @Update
    suspend  fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM bt_categories ORDER BY categoryID DESC")
    fun getAllCategories() : LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM bt_categories WHERE categoryID == :categoryID")
    fun getCategory(categoryID: Int) : CategoryEntity?

    @Query("SELECT * FROM bt_categories ORDER BY categoryID DESC")
    fun getRecentCategories() : List<CategoryEntity>



}