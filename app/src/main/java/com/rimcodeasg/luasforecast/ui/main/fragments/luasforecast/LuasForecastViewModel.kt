package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import androidx.lifecycle.*
import com.rimcodeasg.luasforecast.common.*
import com.rimcodeasg.luasforecast.data.models.StopInfo
import com.rimcodeasg.luasforecast.data.repositories.LuasForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class LuasForecastViewModel (
    private val repository: LuasForecastRepository
) : ViewModel() {


    private val _msg = MutableLiveData<String>()
    val msg : LiveData<String>
        get() = _msg

    private val _forecastVisibilityCase = MutableLiveData<ForecastResulVisibilityCase>()
    val forecastVisibilityCase: LiveData<ForecastResulVisibilityCase>
        get() = _forecastVisibilityCase

    private val loadTrigger = MutableLiveData(Unit)

    val forecastResponse : LiveData<SealedResources<StopInfo>> = loadTrigger.switchMap {
        liveData(Dispatchers.IO) {
            try {

                _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.LOADING_FORECAST)
                _msg.postValue("Loading...")

                if (TimeUtilsObj.isFirstHalfOfTheDay()){
                    repository.getLuasForecastMarlborough().collect {
                        if (it.suceeded){
                            _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.FORECAST_LOADED)
                            _msg.postValue(it.stopInfoMsg)
                            emit(it)
                        } else {
                            _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.NO_FORECAST)
                            _msg.postValue(it.stopInfoMsg)
                        }
                    }
                } else {
                    repository.getLuasForecastStillorgan().collect {
                        if (it.suceeded){
                            _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.FORECAST_LOADED)
                            _msg.postValue(it.stopInfoMsg)
                            emit(it)
                        } else {
                            _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.NO_FORECAST)
                            _msg.postValue(it.stopInfoMsg)
                        }
                    }
                }


            } catch (e : Exception){
                _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.NO_FORECAST)
                _msg.postValue("Error reaching LUAS Forecast!")
                emit(SealedResources.Failure(e))
            }
        }
    }

    fun refresh(){
        loadTrigger.value = Unit
    }

    fun showNoInternetError() {
        _forecastVisibilityCase.postValue(ForecastResulVisibilityCase.NO_FORECAST)
        _msg.postValue("No internet connection! \n Please check your internet connectivity and refresh the app")
    }

}