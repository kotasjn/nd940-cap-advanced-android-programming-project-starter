package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(val app: Application, private val dataSource: RepresentativeDataSource) :
    BaseViewModel(app) {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address  = MutableLiveData<Address>()
    val address : LiveData<Address>
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
                showSnackBar.value = result.message
            }
        }
    }

    fun onFindMyRepresentativesButtonClick() {
        viewModelScope.launch {
            showLoading.postValue(true)
            loadRepresentatives("1263 Pacific Ave. Kansas City, KS")
            showLoading.postValue(false)
        }
    }


    fun onGetLocationButtonClick() {

    }

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
