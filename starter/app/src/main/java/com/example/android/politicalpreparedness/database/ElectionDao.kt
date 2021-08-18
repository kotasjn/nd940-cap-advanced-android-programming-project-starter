package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg elections: Election)

    @Query("select * from election_table order by electionDay")
    fun getElections(): List<Election>

    @Query("select * from election_table where id = :id")
    fun getElection(id: Int): Election?

    @Delete
    fun delete(election: Election)

    @Query("DELETE FROM election_table")
    fun clearAll()
}