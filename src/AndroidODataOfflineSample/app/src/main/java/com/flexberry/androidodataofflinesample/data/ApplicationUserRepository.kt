package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.local.daos.ApplicationUserDao
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import com.flexberry.androidodataofflinesample.data.local.entities.asExternalModel
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApplicationUserRepository(
    // TODO через конструктор репозитория будут внедряться local и network DataSources.
    // private val exampleNetworkDataSource: ExampleNetworkDataSource
    private val ApplicationUserLocalDataSource: ApplicationUserDao
) {
    // Будут отдельные методы для Remote и Network DB на получение данных.
    // Методы будут доставать данные соотв из Local и Network датасоурсов, но возвращать всегда в виде представлений (базовых моделей).
    /*
    fun getApplicationUsersOnline(): Flow<List<ApplicationUser>> =
        exampleNetworkDataSource.getApplicationUsers()
            .map { it.map(NetworkApplicationUser::.asEntity.asExternalModel) }
    */
    fun getApplicationUsersOffline(): Flow<List<ApplicationUser>> =
        ApplicationUserLocalDataSource.getApplicationUsers()
            .map { it.map(ApplicationUserEntity::asExternalModel) }

}