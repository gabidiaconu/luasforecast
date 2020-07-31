package com.rimcodeasg.luasforecast.common

import com.rimcodeasg.luasforecast.data.models.StopInfo

sealed class SealedResources<out T: Any> {
    class Loading<out T : Any> : SealedResources<Nothing>()
    data class Success<out T : Any>(val data: StopInfo) : SealedResources<T>()
    data class Failure<out T : Any>(val exception: Exception) : SealedResources<Nothing>()
}