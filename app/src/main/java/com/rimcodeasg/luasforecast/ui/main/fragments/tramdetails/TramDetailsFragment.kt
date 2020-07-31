package com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.rimcodeasg.luasforecast.R
import com.rimcodeasg.luasforecast.common.TimeUtilsObj
import com.rimcodeasg.luasforecast.data.models.Tram
import com.rimcodeasg.luasforecast.databinding.TramDetailsFragmentBinding
import com.rimcodeasg.luasforecast.di.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


class TramDetailsFragment : Fragment(), KodeinAware {

    companion object {

        private const val TRAM_ENTITY : String = "tram_entity"
        private const val BASE_MAPS_URI = "http://maps.google.com/maps?daddr="
        val marlboroughStationLatLng = LatLng(53.349382, -6.257707)
        val stillorganStationLatLng = LatLng(53.279371, -6.210185)

        fun newInstance(tram : Tram) : TramDetailsFragment {
            val args = Bundle()
            args.putSerializable(TRAM_ENTITY, tram)

            val tramDetailsFragment = TramDetailsFragment()
            tramDetailsFragment.arguments = args

            return tramDetailsFragment
        }
    }
    override val kodein by kodein()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: TramDetailsViewModel by kodeinViewModel()
    private lateinit var binding : TramDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.tram_details_fragment,
            container,
            false)

        binding.apply {
            lifecycleOwner = this@TramDetailsFragment
        }

        binding.viewModel = viewModel

        viewModel.selectedTram = arguments?.getSerializable(TRAM_ENTITY) as Tram

        binding.lateLottieAnimationView.playAnimation()
        binding.okLottieAnimationView.playAnimation()

        binding.goToGoogleMapsButton.setOnClickListener {
            val uriString: String
            if (TimeUtilsObj.isFirstHalfOfTheDay()){
                uriString = marlboroughStationLatLng.latitude.toString() + "," + marlboroughStationLatLng.longitude
            } else {
                uriString = stillorganStationLatLng.latitude.toString() + "," + stillorganStationLatLng.longitude
            }
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BASE_MAPS_URI + uriString)
            )
            startActivity(intent)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as AppCompatActivity)

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    //in some rare situations this can be null
                    //better safe than sorry
                    if (location != null){
                        viewModel.calculateDistanceBetweenCurrentLocationAndTramStation(location)
                        binding.invalidateAll()
                    } else {
                        Toast.makeText(activity as AppCompatActivity, getString(R.string.warn_problems_getting_location),Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}