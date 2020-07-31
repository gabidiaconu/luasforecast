package com.rimcodeasg.luasforecast.ui.main.fragments.luasforecast

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

@Suppress("DEPRECATION")
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

        if (isNetworkAvailable(context)) {
            luasForecastViewModel.luasForecastResponse.observe(
                activity as AppCompatActivity,
                Observer { result ->
                    when (result) {
                        is SealedResources.Loading<Any> -> {
                            treatLoadingCallCase()
                        }

                        is SealedResources.Success -> {
                            val currentStopInfo = result.data
                            if (currentStopInfo.diretions.size == 2 &&
                                currentStopInfo.diretions[0].listOfTrams.size == 1 &&
                                currentStopInfo.diretions[1].listOfTrams.size == 1
                            ) {
                                if(TimeUtilsObj.isFirstHalfOfTheDay()){
                                    if (currentStopInfo.diretions[0].listOfTrams[0].dueMins.isEmpty()) {
                                        treatErrorCallCase(currentStopInfo.diretions[1].listOfTrams[0].destination)
                                    } else {
                                        treatSuccessCallCase(currentStopInfo.msg)
                                        tramListAdapter.submitList(currentStopInfo.diretions[1].listOfTrams)
                                    }
                                } else {
                                    if (currentStopInfo.diretions[1].listOfTrams[0].dueMins.isEmpty()) {
                                        treatErrorCallCase(currentStopInfo.diretions[1].listOfTrams[0].destination)
                                    } else {
                                        treatSuccessCallCase(currentStopInfo.msg)
                                        tramListAdapter.submitList(currentStopInfo.diretions[1].listOfTrams)
                                    }
                                }

                            }
                        }

                        is SealedResources.Failure<Any> -> {
                            Log.e("ErrorGettingReviewsFirebase: ", result.toString())
                        }
                    }
                })
        } else {
            treatNoInternetCase()
        }
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
            //TODO
        }

        return binding.root
    }

    override fun onRecyclerViewItemClicked(tram: Tram) {
        (activity as AppCompatActivity).replaceFragment(TramDetailsFragment.newInstance(tram))
    }



    private fun treatLoadingCallCase(){
        binding.tramsRecyclerView.visibility = View.INVISIBLE
        binding.loadingAnimation.visibility = View.VISIBLE
        binding.errorAnimation.visibility = View.INVISIBLE
        binding.loadingAnimation.playAnimation()
    }

    private fun treatSuccessCallCase(msg: String){
        luasForecastViewModel.msg = msg
        binding.tramsRecyclerView.visibility = View.VISIBLE
        binding.loadingAnimation.visibility = View.INVISIBLE
        binding.errorAnimation.visibility = View.INVISIBLE
    }

    private fun treatErrorCallCase(msg : String){
        luasForecastViewModel.msg = msg
        binding.tramsRecyclerView.visibility = View.INVISIBLE
        binding.loadingAnimation.visibility = View.INVISIBLE
        binding.errorAnimation.visibility = View.VISIBLE
        binding.errorAnimation.playAnimation()
        binding.invalidateAll()
    }

    private fun treatNoInternetCase(){
        luasForecastViewModel.msg = getString(R.string.no_internet_warn)
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}