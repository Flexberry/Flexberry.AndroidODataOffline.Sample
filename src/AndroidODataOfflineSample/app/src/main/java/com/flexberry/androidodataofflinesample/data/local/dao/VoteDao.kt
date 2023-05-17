package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.entities.relations.VoteWithUser
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface VoteDao {
    @Insert
    suspend fun insertVote(vote: VoteEntity)

    // Получение всех строк из таблицы Vote
    @Query("SELECT * FROM Vote")
    fun getVotes(): Flow<List<VoteEntity>>

    // Получение конкретного Vote по ключу и привязанного ApplicationUser
    @Query("SELECT * FROM Vote WHERE __primaryKey=:voteId")
    fun getVoteWithUser(voteId: UUID): Flow<VoteWithUser?>
}