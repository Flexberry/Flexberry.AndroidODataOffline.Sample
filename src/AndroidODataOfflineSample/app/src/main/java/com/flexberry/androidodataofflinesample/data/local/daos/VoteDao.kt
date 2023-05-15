package com.flexberry.androidodataofflinesample.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoteDao {
    @Insert
    suspend fun insertVote(vote: VoteEntity)

    @Query("SELECT * FROM Vote")
    fun getVotes(): Flow<List<VoteEntity>>
}