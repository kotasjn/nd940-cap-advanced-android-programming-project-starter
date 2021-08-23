package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepresentativeFragment : BaseFragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 34
    }

    private lateinit var binding: FragmentRepresentativeBinding

    override val viewModel by viewModel<RepresentativeViewModel>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                viewModel.showLoading.value = true
                getLocation()
            } else {
                Snackbar.make(
                    requireView(),
                    R.string.error_location_required, Snackbar.LENGTH_LONG
                ).show()
            }
        }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isEmpty()) return
            val location = locationList.last()
            viewModel.updateLastKnownLocation(location)
            LocationServices
                .getFusedLocationProviderClient(requireContext())
                .removeLocationUpdates(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupRecyclerView()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            enableMyLocation()
        }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.findMyRepresentative()
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        LocationServices
            .getFusedLocationProviderClient(requireContext())
            .removeLocationUpdates(locationCallback)
    }

    private fun setupRecyclerView() {
        binding.representativesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = RepresentativeListAdapter()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            } else {
                viewModel.showSnackBarInt.value = R.string.error_location_permission_required
            }
        }
    }

    private fun enableMyLocation() {
        if (isLocationPermissionGranted()) {
            checkDeviceLocationSettings()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkDeviceLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(requireActivity())

        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnSuccessListener { result ->
            result.locationSettingsStates?.let {
                if (it.isLocationPresent) {
                    getLocation()
                }
            }
        }

        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e(this.javaClass.name, sendEx.toString())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        viewModel.showLoading.value = true

        val locationRequest = LocationRequest.create().apply {
            interval = 8000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}