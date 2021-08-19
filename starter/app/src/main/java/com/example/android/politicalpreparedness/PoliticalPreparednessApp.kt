package com.example.android.politicalpreparedness

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.android.politicalpreparedness.database.LocalDatabase
import com.example.android.politicalpreparedness.election.ElectionsDataSource
import com.example.android.politicalpreparedness.election.ElectionsRepository
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.RepresentativeDataSource
import com.example.android.politicalpreparedness.representative.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PoliticalPreparednessApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            viewModel { ElectionsViewModel(get(), get() as ElectionsDataSource) }
            viewModel { RepresentativeViewModel(get(), get() as RepresentativeDataSource) }
            viewModel { (election: Election) -> VoterInfoViewModel(get(), get(), election) }

            single { ElectionsRepository(get(), get()) as ElectionsDataSource }
            single { RepresentativeRepository(get()) as RepresentativeDataSource }
            single { LocalDatabase.createElectionDao(this@PoliticalPreparednessApp) }
            single { CivicsApi.retrofitService }
            single { ChuckerInterceptor.Builder(this@PoliticalPreparednessApp).build() }
        }

        startKoin {
            androidContext(this@PoliticalPreparednessApp)
            modules(listOf(myModule))
        }
    }
}