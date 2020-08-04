package com.rimcodeasg.luasforecast.data.repositories

import com.rimcodeasg.luasforecast.common.SealedResources
import com.rimcodeasg.luasforecast.data.models.StopInfo
import com.rimcodeasg.luasforecast.data.retrofit.RetrofitSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LuasForecastRepository (
    private val retrofitSource: RetrofitSource
){

    fun getLuasForecastMarlborough() : Flow<SealedResources<StopInfo>> {
        return flow {
            val body =
                retrofitSource.retrofitRequests.getLuasForecast(RetrofitSource.MARL_FORECAST_URL)
            emit(SealedResources.Success<StopInfo>(body))
        }.flowOn(Dispatchers.IO)
    }

    fun getLuasForecastStillorgan() : Flow<SealedResources<StopInfo>>{
        return flow {
            val body =
                retrofitSource.retrofitRequests.getLuasForecast(RetrofitSource.STILL_FORECAST_URL)
            emit(SealedResources.Success<StopInfo>(body))
        }.flowOn(Dispatchers.IO)
    }

}