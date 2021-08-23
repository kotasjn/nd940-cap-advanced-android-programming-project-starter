package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import java.util.*

class RepresentativeViewModel(
    val app: Application,
    private val dataSource: RepresentativeDataSource
) : BaseViewModel(app) {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private suspend fun loadRepresentatives(address: String) {
        when (val result = dataSource.getRepresentatives(address)) {
            is Result.Success -> {
                _representatives.postValue(
                    result.data.offices.flatMap { office ->
                        office.getRepresentatives(result.data.officials)
                    })
            }
            is Result.Error -> {
                if (result.statusCode == 404) {
                    showSnackBarInt.value = R.string.error_no_representatives_found
                } else {
                    showSnackBar.value = result.message
                }
            }
        }
    }

    fun onFindMyRepresentativesButtonClick() {
        findMyRepresentative()
    }

    private fun findMyRepresentative() = viewModelScope.launch {
        showLoading.postValue(true)
        address.value.let {
            if (it != null) {
                loadRepresentatives(it.toFormattedString())
            } else {
                showSnackBarInt.value = R.string.error_address_required
            }
        }
        showLoading.postValue(false)
    }

    fun updateLastKnownLocation(location: Location?) {
        location.let { loc ->
            if (loc != null) {
                _address.value = geoCodeLocation(loc)
                findMyRepresentative()
            } else {
                showSnackBarInt.value = R.string.error_last_location_not_found
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(app, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }
}
