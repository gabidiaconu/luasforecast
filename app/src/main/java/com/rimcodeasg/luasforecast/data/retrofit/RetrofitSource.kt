package com.rimcodeasg.luasforecast.data.retrofit

import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Retrofit

class RetrofitSource {

    companion object {
        const val BASE_API_URL= "https://luasforecasts.rpa.ie/xml/"
        const val MARL_FORECAST_URL = "https://luasforecasts.rpa.ie/xml/get.ashx?action=forecast&stop=mar&encrypt=false"
        const val STILL_FORECAST_URL = "https://luasforecasts.rpa.ie/xml/get.ashx?action=forecast&stop=mar&encrypt=false"
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(TikXmlConverterFactory.create())
            .build()

    }

    val retrofitRequests : RetrofitRequests by lazy {
        retrofit.create(RetrofitRequests::class.java)
    }

}