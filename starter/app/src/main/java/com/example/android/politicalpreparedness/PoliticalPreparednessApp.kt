package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.LocalDatabase
import com.example.android.politicalpreparedness.election.ElectionsDataSource
import com.example.android.politicalpreparedness.election.ElectionsRepository
import com.example.android.politicalpreparedness.election.ElectionsViewModel
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

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel { ElectionsViewModel(get(), get() as ElectionsDataSource) }
            viewModel { RepresentativeViewModel(get(), get() as RepresentativeDataSource) }

            single { ElectionsRepository(get()) as ElectionsDataSource}
            single { RepresentativeRepository(get()) as RepresentativeDataSource}
            single { LocalDatabase.createElectionDao(this@PoliticalPreparednessApp) }
        }

        startKoin {
            androidContext(this@PoliticalPreparednessApp)
            modules(listOf(myModule))
        }
    }

}