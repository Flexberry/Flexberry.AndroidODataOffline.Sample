package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.AppDataLocalDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser
import javax.inject.Inject

class AppDataRepository @Inject constructor(
    @AppDataLocalDataSource private val localDataSource: LocalDataSource<AppDataEntity>
)
{
//    fun getAppDataOffline(): Flow<List<AppDataEntity>> =
//        AppDataLocalDataSource.AppDataDao().getAppData();
            //.map { it.map(AppDataEntity::asExternalModel) }
}