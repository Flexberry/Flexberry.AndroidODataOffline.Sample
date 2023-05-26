package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.MasterLocalDataSource
import com.flexberry.androidodataofflinesample.data.di.MasterNetworkDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.model.Master
import com.flexberry.androidodataofflinesample.data.model.asDataModel
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkMaster
import com.flexberry.androidodataofflinesample.data.query.Filter
import com.flexberry.androidodataofflinesample.data.query.QuerySettings
import java.util.UUID
import javax.inject.Inject

/**
 * Репозиторий для [Master]
 */
class MasterRepository @Inject constructor(
    @MasterNetworkDataSource private val networkDataSource: NetworkDataSource<NetworkMaster>,
    @MasterLocalDataSource private val localDataSource: LocalDataSource<MasterEntity>,
) {
    /**
     * Получение списка мастеров в режиме онлайн.
     *
     * @param count Количество записей.
     * @return [List] of [Master].
     */
    fun getMastersOnline(count: Int? = null): List<Master> {
        var querySettings = QuerySettings()

        if (count != null && count > 0) querySettings = querySettings.top(count)

        return networkDataSource.readObjects(querySettings, NetworkMaster.Views.NetworkMasterE).map { it.asDataModel() }
    }

    /**
     * Получить мастера по ключу в режиме онлайн.
     *
     * @param primaryKey Ключ мастера.
     */
    fun getMasterByPrimaryKeyOnline(primaryKey: UUID): Master? {
        return networkDataSource.readObjects(
            QuerySettings(Filter.equalFilter("__PrimaryKey", primaryKey))
        ).firstOrNull()?.asDataModel()
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
     * Удалить мастеров в режиме онлайн.
     *
     * @param dataObjects Список мастеров.
     */
    fun deleteMastersOnline(dataObjects: List<Master>) {
        networkDataSource.deleteObjects(dataObjects.map { it.asNetworkModel() })
    }

    /**
     * Получение списка мастеров в режиме оффлайн.
     *
     * @param count Количество записей.
     * @return [List] of [Master].
     */
    fun getMastersOffline(count: Int? = null): List<Master> {
        var querySettings = QuerySettings()

        if (count != null && count > 0) querySettings = querySettings.top(count)

        return localDataSource.readObjects(querySettings, MasterEntity.Views.MasterEntityE).map { it.asDataModel() }
    }

    /**
     * Получить мастера в режиме оффлайн.
     *
     * @param primaryKey ключ мастера.
     */
    fun getMasterByPrimaryKeyOffline(primaryKey: UUID): Master? {
        return localDataSource.readObjects(
            QuerySettings(Filter.equalFilter("primarykey", primaryKey))
        ).firstOrNull()?.asDataModel()
    }

    /**
     * Сохранение мастеров в режиме оффлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateMastersOffline(dataObjects: List<Master>) {
        localDataSource.updateObjects(dataObjects.map { it.asLocalModel() })
    }

    /**
     * Удалить мастеров в режиме оффлайн.
     *
     * @param dataObjects Список мастеров.
     */
    fun deleteMastersOffline(dataObjects: List<Master>) {
        localDataSource.deleteObjects(dataObjects.map { it.asLocalModel() })
    }
}