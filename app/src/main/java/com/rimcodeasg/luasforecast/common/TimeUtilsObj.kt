package com.rimcodeasg.luasforecast.common

import java.util.*

//Singleton patterns for time methods
object TimeUtilsObj {

    // Checking if the current time is in the first half of the day
    // if time is from 00:00 - 12:00 method returns true
    // else if time si from 12:01 to 23:59 method return false
    fun isFirstHalfOfTheDay() : Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)

        return hour < 12 || (hour == 12 && minute < 1)
    }
}