package com.rimcodeasg.luasforecast.common

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.rimcodeasg.luasforecast.R

class DataBindingHelper {

    companion object {

        private const val UNAVAILABLE_TRAM : String = "Tram is DUE, or it will be until you reach the station "
        private const val AVAILABLE_TRAM : String = "You can do it!"

        @JvmStatic
        @BindingAdapter(value = ["setRvAdapter"])
        fun RecyclerView.setRvAdapter(adapter: RecyclerView.Adapter<*>){
            this.run {
                this.adapter = adapter
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setVisibilityState"])
        fun LottieAnimationView.setVisibilityState(minutesDifference : String){
            if (minutesDifference.toDouble() >= 1 ){
                when(this.id){
                    R.id.late_lottie_animation_view ->
                        this.visibility = View.INVISIBLE
                    R.id.ok_lottie_animation_view -> {
                        this.visibility = View.VISIBLE
                        this.playAnimation()}
                 }
            } else {
                when(this.id){
                    R.id.late_lottie_animation_view ->{
                        this.visibility = View.VISIBLE
                        this.playAnimation()}
                    R.id.ok_lottie_animation_view ->
                        this.visibility = View.INVISIBLE
                }
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setMessage"])
        fun TextView.setMessage(minutesDifference: String){
            if (minutesDifference.toDouble() >= 1 ){
                this.text = AVAILABLE_TRAM
            } else {
                this.text = UNAVAILABLE_TRAM
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setVisibilityState"])
        fun LottieAnimationView.setVisibilityCase(forecastResultVisibilityCase: ForecastResulVisibilityCase?){
            forecastResultVisibilityCase.let {
                when(this.id){
                    R.id.loading_animation -> {
                        this.visibility = when(it?.visibilityCase){
                            ForecastResulVisibilityCase.VisibilityCase.LOADING_FORECAST -> View.VISIBLE
                            ForecastResulVisibilityCase.VisibilityCase.FORECAST_LOADED -> View.INVISIBLE
                            ForecastResulVisibilityCase.VisibilityCase.NO_FORECAST -> View.INVISIBLE
                            else ->  View.INVISIBLE
                        }
                        if (this.visibility == View.VISIBLE){
                            playAnimation()
                        }
                    }
                    R.id.error_animation -> {
                        this.visibility = when(it?.visibilityCase){
                            ForecastResulVisibilityCase.VisibilityCase.LOADING_FORECAST -> View.INVISIBLE
                            ForecastResulVisibilityCase.VisibilityCase.FORECAST_LOADED -> View.INVISIBLE
                            ForecastResulVisibilityCase.VisibilityCase.NO_FORECAST -> View.VISIBLE
                            else ->  View.INVISIBLE
                        }
                        if (this.visibility == View.VISIBLE){
                            playAnimation()
                        }
                    }
                }

            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setHeaderMsg"])
        fun TextView.setHeaderMsg(msg : String?){
            this.text = msg
        }

        @JvmStatic
        @BindingAdapter(value = ["setRecyclerViewVisibilityCase"])
        fun RecyclerView.setRecyclerViewVisibilityCase(forecastResultVisibilityCase: ForecastResulVisibilityCase?) {
            forecastResultVisibilityCase.let {
                this.visibility = when (it?.visibilityCase) {
                    ForecastResulVisibilityCase.VisibilityCase.LOADING_FORECAST -> View.INVISIBLE
                    ForecastResulVisibilityCase.VisibilityCase.FORECAST_LOADED -> View.VISIBLE
                    ForecastResulVisibilityCase.VisibilityCase.NO_FORECAST -> View.INVISIBLE
                    else -> View.INVISIBLE
                }
            }
        }
    }
}