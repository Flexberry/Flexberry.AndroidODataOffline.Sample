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
)
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    //private val ApplicationUserLocalDataSource: ApplicationUserRoomDataSource
{
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).

    /**
     * Получение списка пользователей в режиме онлайн.
     *
     * @return [List] of [ApplicationUser].
     */
    fun getApplicationUsersOnline() = networkDataSource.readObjects().map { it.asDataModel() }

    //fun getApplicationUsersOffline(): Flow<List<ApplicationUserEntity>> =
    //    ApplicationUserLocalDataSource.ApplicationUserDao().getApplicationUsers();
            //.map { it.map(ApplicationUserEntity::asExternalModel) }

}