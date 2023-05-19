package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity

@Dao
interface VoteDao {
    @Insert
    fun insertObjects(votes: List<VoteEntity>) : List<Long>

    @RawQuery
    fun getObjects(query: SimpleSQLiteQuery) : List<VoteEntity>

    // TODO реализовать со связью с юзером
    // @RawQuery
    // fun getVoteWithUser(voteIds: List<UUID>): List<VoteWithUser?>

    @Update
    fun updateObjects(votes: List<VoteEntity>): Int

    @Delete
    fun deleteObjects(votes: List<VoteEntity>): Int
}