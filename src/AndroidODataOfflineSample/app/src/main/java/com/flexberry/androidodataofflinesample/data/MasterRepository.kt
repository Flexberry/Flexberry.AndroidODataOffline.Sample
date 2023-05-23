package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.MasterNetworkDataSource
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.data.model.asDataModel
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import javax.inject.Inject

/**
 * Репозиторий для [Master]
 */
class MasterRepository @Inject constructor(
    @MasterNetworkDataSource private val networkDataSource: NetworkDataSource<NetworkMaster>,
) {
    /**
     * Получение списка мастеров в режиме онлайн.
     *
     * @return [List] of [Master].
     */
    fun getMastersOnline(): List<Master> {
        return networkDataSource.readObjects().map { it.asDataModel() }
    }

    /**
     * Сохранение мастеров в режиме онлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateMastersOnline(dataObjects: List<Master>) {
        networkDataSource.updateObjects(dataObjects.map { it.asNetworkModel() })
    }

    /**
     * Получение списка мастеров в режиме оффлайн.
     *
     * @return [List] of [Master].
     */
    fun getMastersOffline(): List<Master> {
        return emptyList()
    }

    /**
     * Сохранение мастеров в режиме оффлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateMastersOffline(dataObjects: List<Master>) {
        //localDataSource.updateObjects(dataObjects.map { it.asLocalModel() })
    }
}