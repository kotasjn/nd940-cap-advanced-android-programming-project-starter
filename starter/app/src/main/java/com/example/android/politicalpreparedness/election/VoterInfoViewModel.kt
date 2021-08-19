package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    val app: Application,
    private val dataSource: ElectionsDataSource,
    private val election: Election
) : BaseViewModel(app) {

    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()
    val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo

    private val _isFollowing = MutableLiveData<Boolean?>()
    val isFollowing: LiveData<Boolean?>
        get() = _isFollowing

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    init {
        viewModelScope.launch {
            checkElectionSaved()
            loadVoterInfo()
        }
    }

    fun openBrowser(url: String) {
        _url.value = url
    }

    fun closeBrowser() {
        _url.value = null
    }

    fun onFollowButtonClick() = viewModelScope.launch {
        isFollowing.value.let { isFollowing ->
            if (isFollowing != null && isFollowing) {
                dataSource.deleteElection(election)
            } else {
                dataSource.saveElection(election)
            }
        }
        checkElectionSaved()
    }

    private suspend fun checkElectionSaved() {
        showLoading.postValue(true)
        when (val result = dataSource.getElection(election.id)) {
            is Result.Success -> {
                _isFollowing.postValue(true)
            }
            is Result.Error -> {
                _isFollowing.postValue(false)
                result.message?.let { showErrorMessage.postValue(it) }
            }
        }
        showLoading.postValue(false)
    }

    private suspend fun loadVoterInfo() {
        showLoading.postValue(true)
        val address = if (election.division.state.isNotBlank())
            "${election.division.country}, ${election.division.state}"
        else election.division.country
        when (val result = dataSource.getVoterInfo(address, election.id)) {
            is Result.Success -> {
                _voterInfo.postValue(result.data)
            }
            is Result.Error -> {
                _voterInfo.value = VoterInfoResponse(election)
                showSnackBar.value = result.message
            }
        }
        showLoading.postValue(false)
    }
}