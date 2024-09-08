package com.jd.qbytez.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jd.qbytez.R
import com.jd.qbytez.databinding.FragmentSettingsBinding
import com.jd.qbytez.view.activities.MainActivity


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        var sharedPreferences =
            requireActivity().getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)

        binding.darkModeSwitch.isOn = !isDarkModeOn

        binding.darkModeSwitch.setOnToggledListener { _, isOn ->

            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isDarkModeOn", false)
                editor.apply()

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isDarkModeOn", true)
                editor.apply()

            }
        }

        binding.clCategories.setOnClickListener {
            (requireActivity() as MainActivity).pushFragments(CategoriesFragment())
        }

    }




    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigation(true)
            (activity as MainActivity).showAppBar(true)
            (activity as MainActivity).showTitle(getString(R.string.title_settings))
        }
    }



}