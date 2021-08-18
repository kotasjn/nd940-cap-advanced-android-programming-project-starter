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

    // TODO get voter info from API
    init {
        viewModelScope.launch {
            checkElectionSaved()
        }

        val voterInfoResponse = VoterInfoResponse(
            election,
            "location",
            "contents"
        )
        _voterInfo.value = voterInfoResponse
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
        when(val result = dataSource.getElection(election.id)) {
            is Result.Success -> {
                _isFollowing.postValue(true)
            }
            is Result.Error -> {
                _isFollowing.postValue(false)
                result.message?.let { showErrorMessage.postValue(it) }
            }
        }
    }
}