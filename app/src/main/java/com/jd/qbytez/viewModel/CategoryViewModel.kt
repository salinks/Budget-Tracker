package com.jd.qbytez.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel (private val categoryRepository: CategoryRepository): ViewModel() {

    val allCategories: LiveData<List<CategoryEntity>> =categoryRepository.getCategories()

    fun insert(category: CategoryEntity)=viewModelScope.launch {
        categoryRepository.insertCategory(category)
    }
    fun update(category: CategoryEntity)=viewModelScope.launch {
        categoryRepository.updateCategory(category)
    }
    fun delete(category: CategoryEntity)=viewModelScope.launch {
        categoryRepository.deleteCategory(category)
    }




}
class CategoryViewModelFactory(private val categoryRepository: CategoryRepository):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}