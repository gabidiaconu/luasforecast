package com.rimcodeasg.luasforecast.ui.main.fragments.tramdetails

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
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
import com.rimcodeasg.luasforecast.ui.splash.SplashActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


class TramDetailsFragment : Fragment(), KodeinAware {

    companion object {

        private const val TRAM_ENTITY : String = "tram_entity"
        private const val BASE_MAPS_URI = "http://maps.google.com/maps?daddr="

        /*
        * LatLong of the Marlborough Tram Station
        * */
        val marlboroughStationLatLng = LatLng(53.349382, -6.257707)

        /*
        * LatLong of the Stillorgan Tram Station
        * */
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

        /*
        * Opening location with google maps
        * so the user can see how far he actually is
        * from the tram station
        * */
        binding.goToGoogleMapsButton.setOnClickListener {
            val uriString: String = if (TimeUtilsObj.isFirstHalfOfTheDay()){
                marlboroughStationLatLng.latitude.toString() + "," + marlboroughStationLatLng.longitude
            } else {
                stillorganStationLatLng.latitude.toString() + "," + stillorganStationLatLng.longitude
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
            requestPermissions(
                activity!!, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS
            )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(SplashActivity.TAG, getString(R.string.info_permission_callback_called))

        when (requestCode) {
            SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    // Check for both permissions
                    if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(SplashActivity.TAG, getString(R.string.info_location_permissions_granted))
                    } else {
                        Log.d(SplashActivity.TAG, getString(R.string.warn_some_permissions_not_granted))
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if ( ActivityCompat.shouldShowRequestPermissionRationale(activity as AppCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale(activity as AppCompatActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            showDialogOK(DialogInterface.OnClickListener { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        requestPermissions(
                                            activity!!, arrayOf(
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                            ),
                                            SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS
                                        )
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> activity!!.finish()
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity as AppCompatActivity)
            .setMessage(getString(R.string.warn_service_permissions_required))
            .setPositiveButton(getString(R.string.label_ok), okListener)
            .setNegativeButton(getString(R.string.label_cancel), okListener)
            .create()
            .show()
    }

}