package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse

interface ElectionsDataSource {

    suspend fun getElection(id: Int): LiveData<Election?>

    suspend fun saveElection(election: Election)

    suspend fun deleteElection(election: Election)

    suspend fun getUpcomingElections(): LiveData<ElectionResponse>

    suspend fun getSavedElections(): LiveData<List<Election>>

}