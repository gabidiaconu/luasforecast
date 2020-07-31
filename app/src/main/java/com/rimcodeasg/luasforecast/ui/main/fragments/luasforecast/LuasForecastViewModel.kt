package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.rimcodeasg.luasforecast.common.SealedResources
import com.rimcodeasg.luasforecast.common.TimeUtilsObj
import com.rimcodeasg.luasforecast.data.models.StopInfo
import com.rimcodeasg.luasforecast.data.repositories.LuasForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class LuasForecastViewModel (
    private val repository: LuasForecastRepository
) : ViewModel() {

    var msg : String = "Loading"

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    val luasForecastResponse : LiveData<SealedResources<StopInfo>> = liveData(Dispatchers.IO){

        try {

            if (TimeUtilsObj.isFirstHalfOfTheDay()){
                repository.getLuasForecastMarlborough().collect {
                    emit(it)
                }
            } else {
                repository.getLuasForecastStillorgan().collect {
                    emit(it)
                }
            }


        } catch (e : Exception){
            Log.e("TestGabi", e.toString())
        }
    }

}