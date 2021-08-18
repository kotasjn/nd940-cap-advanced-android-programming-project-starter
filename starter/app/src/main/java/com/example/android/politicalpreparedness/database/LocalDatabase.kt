package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Room

/**
 * Singleton class that is used to create a election db
 */
object LocalDatabase {

    fun createElectionDao(context: Context): ElectionDao {
        return Room.databaseBuilder(
            context.applicationContext,
            ElectionDatabase::class.java, "election_database"
        ).build().electionDao()
    }

}