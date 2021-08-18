package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ElectionsRepository(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ElectionsDataSource {

    override suspend fun getElection(id: Int): LiveData<Election?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveElection(election: Election) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteElection(election: Election) {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingElections(): LiveData<ElectionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedElections(): LiveData<List<Election>> {
        TODO("Not yet implemented")
    }

}