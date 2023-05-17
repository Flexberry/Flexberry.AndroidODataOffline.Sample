package com.flexberry.androidodataofflinesample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.relations.ApplicationUserWithVotes
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationUserDao {
    @Insert
    suspend fun insertApplicationUser(applicationUser: ApplicationUserEntity)

    @Query("SELECT * FROM ApplicationUser")
    fun getApplicationUsers(): Flow<List<ApplicationUserEntity>>

    @Transaction
    @Query("SELECT * FROM ApplicationUser")
    fun getUsersWithVotes(): Flow<List<ApplicationUserWithVotes>>
}