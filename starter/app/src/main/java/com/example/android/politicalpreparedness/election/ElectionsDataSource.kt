package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface ElectionsDataSource {

    suspend fun getElection(id: Int): Result<Election>

    suspend fun saveElection(election: Election)

    suspend fun deleteElection(election: Election)

    suspend fun getUpcomingElections(): Result<ElectionResponse>

    suspend fun getSavedElections(): Result<List<Election>>

    suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse>

}