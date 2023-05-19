package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.VoteNetworkDatasource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import javax.inject.Inject

class VoteRepository @Inject constructor(
    @VoteNetworkDatasource private val networkDataSource: NetworkDataSource<NetworkVote>
)
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    // private val exampleLocalDataSource: ExampleLocalDataSource
{
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getVotesOnline(): Flow<List<Vote>> =
        exampleNetworkDataSource.getVotes()
            .map { it.map(NetworkVote::.asEntity.asExternalModel) }
    */

    //fun getVotesOffline(): Flow<List<VoteEntity>> =
        //VoteLocalDataSource.VoteDao().getVotes();
            //.map { it.map(VoteEntity::asExternalModel) }
}