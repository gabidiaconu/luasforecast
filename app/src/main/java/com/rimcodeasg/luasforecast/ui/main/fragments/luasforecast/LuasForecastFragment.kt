package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rimcodeasg.luasforecast.R
import com.rimcodeasg.luasforecast.common.SealedResources
import com.rimcodeasg.luasforecast.common.TimeUtilsObj
import com.rimcodeasg.luasforecast.data.models.Tram
import com.rimcodeasg.luasforecast.databinding.FragmentLuasForecastBinding
import com.rimcodeasg.luasforecast.di.kodeinViewModel
import com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails.TramDetailsFragment
import com.rimcodeasg.luasforecast.ui.main.replaceFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class LuasForecastFragment : Fragment() , KodeinAware, IRecyclerViewItemClickListener {

    companion object{
        @JvmStatic
        fun newInstance() =
            LuasForecastFragment()
    }

    override val kodein by kodein()
    private val luasForecastViewModel: LuasForecastViewModel by kodeinViewModel()
    private lateinit var binding : FragmentLuasForecastBinding

    private val tramListAdapter = TramListAdapter(this)

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        luasForecastViewModel.forecastResponse.observe(
            activity as AppCompatActivity,
            Observer { result ->
                when (result) {
                    is SealedResources.Loading -> {
                        Log.e("RetrofitLuasForecastResponse:  ", "Loading...")
                    }
                    is SealedResources.Success -> {
                        val currentStopInfo = result.data
                        if (TimeUtilsObj.isFirstHalfOfTheDay()){
                            tramListAdapter.submitList(currentStopInfo.diretions[0].listOfTrams)
                        } else {
                            tramListAdapter.submitList(currentStopInfo.diretions[1].listOfTrams)
                        }
                    }
                    is SealedResources.Failure -> {
                        Log.e("RetrofitLuasForecastResponseError: ", result.toString())
                        if (!isOnline(context!!)){
                            luasForecastViewModel.showNoInternetError()
                        }
                    }
                }
            })
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_luas_forecast,
            container,
            false
        )

        binding.apply {
            lifecycleOwner = this@LuasForecastFragment
        }

        binding.luasForecastViewModel = luasForecastViewModel

        binding.tramListAdapter = tramListAdapter

        binding.tramsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.refreshButton.setOnClickListener {
            luasForecastViewModel.refresh()
        }

        return binding.root
    }

    override fun onRecyclerViewItemClicked(tram: Tram) {
        if (tram.dueMins.isNotEmpty() || tram.dueMins.isNotBlank()) {
            (activity as AppCompatActivity).replaceFragment(TramDetailsFragment.newInstance(tram))
        } else {
            Toast.makeText(context,"Tram not available!", Toast.LENGTH_LONG).show()
        }
    }


    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}