package com.example.android.politicalpreparedness.representative

import android.app.Application
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RepresentativeRepository(
    private val application: Application,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO): RepresentativeDataSource {

}