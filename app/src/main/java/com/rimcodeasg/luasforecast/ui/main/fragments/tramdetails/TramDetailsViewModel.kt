package com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.rimcodeasg.luasforecast.common.TimeUtilsObj
import com.rimcodeasg.luasforecast.data.models.Tram
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsFragment.Companion.marlboroughStationLatLng
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsFragment.Companion.stillorganStationLatLng
import java.math.BigDecimal

class TramDetailsViewModel : ViewModel() {

    lateinit var selectedTram : Tram


    private val _distanceToTramStation = MutableLiveData<Double>()
    val distanceToTramStation : LiveData<Double>
        get() = _distanceToTramStation

    private var _differenceInMinutes = MutableLiveData<Double>()
    val differenceInMinutes : LiveData<Double>
        get() = _differenceInMinutes

    fun calculateDistanceBetweenCurrentLocationAndTramStation(location: Location){

        val currentLatLng = LatLng(location.latitude, location.longitude)

        if (TimeUtilsObj.isFirstHalfOfTheDay()){
            val sphericalDistanceBetweenCurrentLatLngAndStation = BigDecimal.valueOf(SphericalUtil.computeDistanceBetween(currentLatLng, marlboroughStationLatLng))
                .setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
            _distanceToTramStation.postValue(sphericalDistanceBetweenCurrentLatLngAndStation)
            _differenceInMinutes.postValue(calculateDifferenceInMinutesFromCurrentLocationToStation(sphericalDistanceBetweenCurrentLatLngAndStation))
        } else {
            val sphericalDistanceBetweenCurrentLatLngAndStation = BigDecimal.valueOf(SphericalUtil.computeDistanceBetween(currentLatLng, stillorganStationLatLng))
                .setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
            _distanceToTramStation.postValue(sphericalDistanceBetweenCurrentLatLngAndStation)
            _differenceInMinutes.postValue(calculateDifferenceInMinutesFromCurrentLocationToStation(sphericalDistanceBetweenCurrentLatLngAndStation))
        }
    }

    private fun calculateDifferenceInMinutesFromCurrentLocationToStation(sphericalDistanceBetweenCurrentLatLngAndStation : Double) : Double{
        val minutesToStation = BigDecimal.valueOf(sphericalDistanceBetweenCurrentLatLngAndStation / 1.3)
            .setScale(2, BigDecimal.ROUND_HALF_UP)
            .toDouble()
        return selectedTram.dueMins.toDouble() - minutesToStation
    }

}