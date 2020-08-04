package com.rimcodeasg.luasforecast.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rimcodeasg.luasforecast.R
import com.rimcodeasg.luasforecast.databinding.ActivityMainBinding
import com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast.LuasForecastFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var doubleBackToExitPressedOnce: Boolean = false

    private lateinit var currentFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        this.replaceFragment(LuasForecastFragment.newInstance())
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        currentFragment = fragment
    }

    override fun onBackPressed() {

        if (currentFragment is LuasForecastFragment) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
            }
            this.doubleBackToExitPressedOnce = true
            GlobalScope.launch {
                delay(2000)
                doubleBackToExitPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }
}

// Extension function to replace fragment
fun AppCompatActivity.replaceFragment(fragment: Fragment){
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container,fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}