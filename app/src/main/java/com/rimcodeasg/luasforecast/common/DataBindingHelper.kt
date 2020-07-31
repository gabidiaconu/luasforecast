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
        @BindingAdapter(value = ["setAdapter"])
        fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>){
            this.run {
                this.adapter = adapter
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setVisibilityState"])
        fun LottieAnimationView.setVisibilityState(minutesDiference : String){
            if (minutesDiference.toDouble() >= 1 ){
                when(this.id){
                    R.id.late_lottie_animation_view -> this.visibility = View.INVISIBLE
                    R.id.ok_lottie_animation_view -> this.visibility = View.VISIBLE
                 }
            } else {
                when(this.id){
                    R.id.late_lottie_animation_view -> this.visibility = View.VISIBLE
                    R.id.ok_lottie_animation_view -> this.visibility = View.INVISIBLE
                }
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["setMessage"])
        fun TextView.setMessage(minutesDiference: String){
            if (minutesDiference.toDouble() >= 1 ){
                this.text = AVAILABLE_TRAM
            } else {
                this.text = UNAVAILABLE_TRAM
            }
        }

    }
}