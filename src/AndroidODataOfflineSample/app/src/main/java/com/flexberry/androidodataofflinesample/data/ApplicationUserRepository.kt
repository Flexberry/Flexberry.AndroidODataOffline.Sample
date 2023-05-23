package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.ApplicationUserLocalDataSource
import com.flexberry.androidodataofflinesample.data.di.ApplicationUserNetworkDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import com.flexberry.androidodataofflinesample.data.model.asDataModel
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import javax.inject.Inject

class ApplicationUserRepository @Inject constructor(
    @ApplicationUserNetworkDataSource private val networkDataSource: NetworkDataSource<NetworkApplicationUser>,
    @ApplicationUserLocalDataSource private val localDataSource: LocalDataSource<ApplicationUserEntity>
) {
    /**
     * Получение списка пользователей в режиме онлайн.
     *
     * @return [List] of [ApplicationUser].
     */
    fun getApplicationUsersOnline(): List<ApplicationUser> {
        return networkDataSource.readObjects().map { it.asDataModel() }
    }

    /**
     * Сохранение пользователей в режиме онлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateApplicationUsersOnline(dataObjects: List<ApplicationUser>) {
        networkDataSource.updateObjects(dataObjects.map { it.asNetworkModel() })
    }

    /**
     * Получение списка пользователей в режиме оффлайн.
     *
     * @return [List] of [ApplicationUser].
     */
    fun getApplicationUsersOffline(): List<ApplicationUser> {
        // Тут нужно взять данные из локального источника данных.
        return emptyList()
    }

    /**
     * Сохранение пользователей в режиме оффлайн.
     *
     * @param dataObjects Список объектов.
     */
    fun updateApplicationUsersOffline(dataObjects: List<ApplicationUser>) {
        localDataSource.updateObjects(dataObjects.map { it.asLocalModel() })
    }
}