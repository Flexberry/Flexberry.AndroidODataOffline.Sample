package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.VoteLocalDatasource
import com.flexberry.androidodataofflinesample.data.di.VoteNetworkDatasource
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.model.Vote
import com.flexberry.androidodataofflinesample.data.model.asDataModel
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import javax.inject.Inject

class VoteRepository @Inject constructor(
    @VoteNetworkDatasource private val networkDataSource: NetworkDataSource<NetworkVote>,
    @VoteLocalDatasource private val localDataSource:LocalDataSource<VoteEntity>
) {
    /**
     * Получение списка голосов в режиме онлайн.
     *
     * @return [List] of [Vote].
     */
    fun getVotesOnline(): List<Vote> {
        return networkDataSource.readObjects().map { it.asDataModel() }
    }

    /**
     * Сохранение голосов в режиме онлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateVotesOnline(dataObjects: List<Vote>) {
        networkDataSource.updateObjects(dataObjects.map { it.asNetworkModel() })
    }

    /**
     * Получение списка голосов в режиме оффлайн.
     *
     * @return [List] of [Vote].
     */
    fun getVotesOffline(): List<Vote> {
        // Тут нужно взять данные из локального источника данных.
        return emptyList()
    }

    /**
     * Сохранение голосов в режиме оффлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateVotesOffline(dataObjects: List<Vote>) {
        // Тут нужно отправить данные в локальный источник данных.
    }
}