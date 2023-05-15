package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.local.daos.VoteDao
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.entities.asExternalModel
import com.flexberry.androidodataofflinesample.data.model.Vote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VoteRepository(
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    private val VoteLocalDataSource: VoteDao
) {
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getVotesOnline(): Flow<List<Vote>> =
        exampleNetworkDataSource.getVotes()
            .map { it.map(NetworkVote::.asEntity.asExternalModel) }
    */

    fun getVotesOffline(): Flow<List<Vote>> =
        VoteLocalDataSource.getVotes()
            .map { it.map(VoteEntity::asExternalModel) }


}