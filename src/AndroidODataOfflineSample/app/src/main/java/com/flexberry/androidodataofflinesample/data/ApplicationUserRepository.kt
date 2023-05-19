package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.ApplicationUserNetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.interfaces.NetworkDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import javax.inject.Inject

class ApplicationUserRepository @Inject constructor(
    @ApplicationUserNetworkDataSource private val networkDataSource: NetworkDataSource<NetworkApplicationUser>
)
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    //private val ApplicationUserLocalDataSource: ApplicationUserRoomDataSource
{
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getApplicationUsersOnline(): Flow<List<ApplicationUser>> =
        exampleNetworkDataSource.getApplicationUsers()
            .map { it.map(NetworkApplicationUser::.asEntity.asExternalModel) }
    */
    //fun getApplicationUsersOffline(): Flow<List<ApplicationUserEntity>> =
    //    ApplicationUserLocalDataSource.ApplicationUserDao().getApplicationUsers();
            //.map { it.map(ApplicationUserEntity::asExternalModel) }

}