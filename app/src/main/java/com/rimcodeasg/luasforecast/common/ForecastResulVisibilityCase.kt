package com.rimcodeasg.luasforecast.common

@Suppress("DataClassPrivateConstructor")
data class ForecastResulVisibilityCase private constructor(val visibilityCase: VisibilityCase){

    companion object {
        val LOADING_FORECAST = ForecastResulVisibilityCase(VisibilityCase.LOADING_FORECAST)
        val FORECAST_LOADED = ForecastResulVisibilityCase(VisibilityCase.FORECAST_LOADED)
        val NO_FORECAST = ForecastResulVisibilityCase(VisibilityCase.NO_FORECAST)
    }

    enum class VisibilityCase{
        LOADING_FORECAST,
        FORECAST_LOADED,
        NO_FORECAST
    }
}