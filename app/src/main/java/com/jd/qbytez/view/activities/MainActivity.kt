package com.jd.qbytez.view.activities

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jd.qbytez.R
import com.jd.qbytez.databinding.ActivityMainBinding
import com.jd.qbytez.view.fragments.AddTransactionFragment
import com.jd.qbytez.view.fragments.HomeFragment
import com.jd.qbytez.view.fragments.TransactionsFragment
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        pushFragments(HomeFragment())


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bn_home -> {
                    pushFragments(HomeFragment())
                    true
                }

                R.id.bn_transactions -> {

                    pushFragments(TransactionsFragment())
                    true
                }


                else -> {
                    false
                }
            }
        }

        binding.ivBack.setOnClickListener {
           handleBackPressed()
        }

    }



    fun pushFragments(fragment: Fragment) {
        if(fragment is HomeFragment){
            while(supportFragmentManager.backStackEntryCount > 0) { supportFragmentManager.popBackStackImmediate(); }
        }

        val supFragMan = supportFragmentManager
        supFragMan.beginTransaction()
            .setCustomAnimations( R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out )
            .replace(R.id.content_frame, fragment, fragment.javaClass.name)
            .addToBackStack(fragment.javaClass.name)
            .commitAllowingStateLoss()

    }

    fun showBottomNavigation(show: Boolean) {
        if (show) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

    fun showAppBar(show: Boolean) {
        if (show) {
            binding.relHeader.visibility = View.VISIBLE
        } else {
            binding.relHeader.visibility = View.GONE
        }

    }

    fun showTitle(mTitle: String) {
        binding.appTitle.text = mTitle
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPressed()
        }
    }

     fun handleBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (f is HomeFragment) {
            val fragment1 =
                supportFragmentManager.findFragmentById(R.id.content_frame) as HomeFragment?
            if (fragment1 != null && fragment1.isVisible) {
                notifyBackPress()
            }
        }
        else if (f is TransactionsFragment) {
            val fragment1 =
                supportFragmentManager.findFragmentById(R.id.content_frame) as TransactionsFragment?
            if (fragment1 != null && fragment1.isVisible) {
                binding.bottomNavigationView.selectedItemId =R.id.bn_home
                pushFragments(HomeFragment())
            }
        }

        else{
          supportFragmentManager.popBackStack()
        }

    }

    private fun notifyBackPress() {
    exitProcess(0)
    }


}