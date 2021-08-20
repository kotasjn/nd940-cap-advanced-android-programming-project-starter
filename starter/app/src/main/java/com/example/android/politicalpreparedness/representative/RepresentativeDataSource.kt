package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.network.Result
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse

interface RepresentativeDataSource {

    suspend fun getRepresentatives(address: String): Result<RepresentativeResponse>

}