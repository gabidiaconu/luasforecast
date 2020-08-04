package com.rimcodeasg.luasforecast.common

import com.rimcodeasg.luasforecast.data.models.StopInfo

sealed class SealedResources<out T: Any> {
    object Loading : SealedResources<Nothing>()
    data class Success<out T : Any>(val data: StopInfo) : SealedResources<T>()
    data class Failure(val exception: Exception) : SealedResources<Nothing>()
}

val SealedResources<*>.suceeded
    get() = this is SealedResources.Success

val SealedResources<*>.stopInfoMsg
    get() = (this as? SealedResources.Success<*>)?.data!!.msg
