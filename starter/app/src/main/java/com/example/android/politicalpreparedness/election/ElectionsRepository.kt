package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ElectionsRepository(
    private val electionDao: ElectionDao,
    private val civicsApiService: CivicsApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionsDataSource {

    override suspend fun getElection(id: Int): Result<Election> = withContext(dispatcher) {
        try {
            val election = electionDao.getElection(id)
            if (election != null) {
                return@withContext Result.Success(election)
            } else {
                return@withContext Result.Error()
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun saveElection(election: Election) = withContext(dispatcher) {
        electionDao.insert(election)
    }

    override suspend fun deleteElection(election: Election) = withContext(dispatcher) {
        electionDao.delete(election)
    }

    override suspend fun getUpcomingElections(): Result<ElectionResponse> = withContext(dispatcher) {
        return@withContext try {
            Result.Success(civicsApiService.getElections())
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }

    override suspend fun getSavedElections(): Result<List<Election>> = withContext(dispatcher) {
        return@withContext try {
            Result.Success(electionDao.getElections())
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Int): Result<VoterInfoResponse> = withContext(dispatcher) {
        return@withContext try {
            Result.Success(civicsApiService.getVoterInfo(address, electionId))
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }
}