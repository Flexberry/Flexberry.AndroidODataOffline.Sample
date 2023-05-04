package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.model.Vote
import kotlinx.coroutines.flow.Flow

class VoteRepository(
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    // private val exampleLocalDataSource: ExampleLocalDataSource
) {
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getVotesOnline(): Flow<List<Vote>> =
        exampleNetworkDataSource.getVotes()
            .map { it.map(NetworkVote::.asEntity.asExternalModel) }

    fun getVotesOffline(): Flow<List<Vote>> =
        exampleLocalDataSource.getVotes()
            .map { it.map(VoteEntity::asExternalModel) }

     */
}