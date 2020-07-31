package com.rimcodeasg.luasforecast.data.retrofit

import com.rimcodeasg.luasforecast.data.models.StopInfo
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitRequests {

    @GET
    suspend fun getLuasForecast(@Url url: String?): StopInfo
}