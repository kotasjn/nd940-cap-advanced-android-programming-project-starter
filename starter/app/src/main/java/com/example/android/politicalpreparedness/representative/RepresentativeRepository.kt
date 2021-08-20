package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativeRepository(
    private val civicsApiService: CivicsApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepresentativeDataSource {

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> =
        withContext(dispatcher) {
            return@withContext try {
                Result.Success(civicsApiService.getRepresentatives(address))
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }
}