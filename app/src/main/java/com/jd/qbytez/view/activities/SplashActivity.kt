package com.jd.qbytez.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.jd.qbytez.application.BudgetTrackerApplication
import com.jd.qbytez.databinding.ActivitySplashBinding
import com.jd.qbytez.models.CategoryEntity
import com.jd.qbytez.viewModel.CategoryViewModel
import com.jd.qbytez.viewModel.CategoryViewModelFactory

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory((application as BudgetTrackerApplication).categoryRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)



        if (isFirstLaunch) {
            addDefaultCategories()
            editor.putBoolean("isFirstLaunch", false)
            editor.apply()
        }

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            );
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            );
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({


            finish()
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }, 2000)

    }

    private fun addDefaultCategories() {
        categoryViewModel.insert(CategoryEntity(0, "Entertainment", "Expense", 596))
        categoryViewModel.insert(CategoryEntity(0, "Transport", "Expense", 397))
        categoryViewModel.insert(CategoryEntity(0, "Food", "Expense", 454))
        categoryViewModel.insert(CategoryEntity(0, "Other Income", "Income", 296))
        categoryViewModel.insert(CategoryEntity(0, "Investment", "Income", 267))
        categoryViewModel.insert(CategoryEntity(0, "Salary", "Income", 259))
    }
}