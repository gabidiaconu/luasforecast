package com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.rimcodeasg.luasforecast.common.TimeUtilsObj
import com.rimcodeasg.luasforecast.data.models.Tram
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsFragment.Companion.marlboroughStationLatLng
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsFragment.Companion.stillorganStationLatLng
import java.math.BigDecimal
import java.math.RoundingMode

class TramDetailsViewModel : ViewModel() {

    lateinit var selectedTram : Tram

    private var _distanceToTramStation : String = "0.0"
    val distanceToTramStation : String
        get() = BigDecimal.valueOf(_distanceToTramStation.toDouble()).setScale(2, RoundingMode.HALF_UP).toPlainString()

    private var _differenceInMinues : Double = -1.0
    val differenceInMinutes : String
        get() = _differenceInMinues.toString()

    fun calculateDistanceBetweenCurrentLocationAndTramStation(location: Location){

        val currentLatLng = LatLng(location.latitude, location.longitude)

        if (TimeUtilsObj.isFirstHalfOfTheDay()){
            _distanceToTramStation = SphericalUtil.computeDistanceBetween(currentLatLng, marlboroughStationLatLng).toString()
            _differenceInMinues = calculateDifferenceInMinutesFromCurrentLocationToStation()
        } else {
            _distanceToTramStation = SphericalUtil.computeDistanceBetween(currentLatLng, stillorganStationLatLng).toString()
        }
    }

    fun calculateDifferenceInMinutesFromCurrentLocationToStation() : Double{
        val distance = _distanceToTramStation.toDouble()
        val minutesToStation = BigDecimal.valueOf(distance / 1.3).setScale(2, RoundingMode.HALF_UP).toDouble()
        return selectedTram.dueMins.toDouble() - minutesToStation
    }

}