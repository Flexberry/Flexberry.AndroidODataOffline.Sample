package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.local.dao.ApplicationUserDao
import com.flexberry.androidodataofflinesample.data.local.datasource.ApplicationUserRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.asExternalModel
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApplicationUserRepository @Inject constructor()
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    private val ApplicationUserLocalDataSource: ApplicationUserRoomDataSource
) {
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getApplicationUsersOnline(): Flow<List<ApplicationUser>> =
        exampleNetworkDataSource.getApplicationUsers()
            .map { it.map(NetworkApplicationUser::.asEntity.asExternalModel) }
    */
    fun getApplicationUsersOffline(): Flow<List<ApplicationUserEntity>> =
        ApplicationUserLocalDataSource.ApplicationUserDao().getApplicationUsers();
            //.map { it.map(ApplicationUserEntity::asExternalModel) }

}