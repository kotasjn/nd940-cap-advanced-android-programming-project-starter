package com.example.android.politicalpreparedness.election

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
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

    override suspend fun saveElection(election: Election) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteElection(election: Election) {
        TODO("Not yet implemented")
    }

    // TODO service
    override suspend fun getUpcomingElections(): Result<ElectionResponse> {
        return Result.Success(
            ElectionResponse(
                "", mutableListOf(
                    Election(
                        1,
                        "Election 1",
                        Calendar.getInstance().time,
                        Division(
                            "1",
                            "country",
                            "state"
                        )
                    )
                )
            )
        )
    }

    override suspend fun getSavedElections(): Result<List<Election>> {
        return Result.Success(
            mutableListOf(
                Election(
                    1,
                    "Election 1",
                    Calendar.getInstance().time,
                    Division(
                        "1",
                        "country",
                        "state"
                    )
                )
            )
        )
    }
}