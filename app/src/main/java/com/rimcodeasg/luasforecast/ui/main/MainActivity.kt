package com.rimcodeasg.luasforecast.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rimcodeasg.luasforecast.R
import com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast.LuasForecastFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.replaceFragment(LuasForecastFragment.newInstance())
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