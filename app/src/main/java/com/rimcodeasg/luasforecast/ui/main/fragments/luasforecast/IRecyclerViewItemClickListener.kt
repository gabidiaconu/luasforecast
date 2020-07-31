package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import com.rimcodeasg.luasforecast.data.models.Tram

interface IRecyclerViewItemClickListener {
    fun onRecyclerViewItemClicked(tram : Tram)
}