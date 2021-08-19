package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(val app: Application, private val dataSource: ElectionsDataSource) :
    BaseViewModel(app) {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    init {
        viewModelScope.launch {
            showLoading.postValue(true)
            loadUpcomingElections()
            showLoading.postValue(false)
        }
    }

    private suspend fun loadUpcomingElections() {
        when (val result = dataSource.getUpcomingElections()) {
            is Result.Success -> {
                _upcomingElections.postValue(result.data.elections)
            }
            is Result.Error -> {
                showSnackBar.value = result.message
            }
        }
    }

    fun navigateToElectionDetail(election: Election) {
        _savedElections.value = listOf()
        navigationCommand.value = NavigationCommand.To(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
        )
    }

    fun loadSavedElections() {
        viewModelScope.launch {
            when (val result = dataSource.getSavedElections()) {
                is Result.Success -> {
                    _savedElections.postValue(result.data)
                }
                is Result.Error -> {
                    showSnackBar.value = result.message
                }
            }
        }
    }
}