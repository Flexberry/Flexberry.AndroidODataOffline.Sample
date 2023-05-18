package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.local.datasource.AppDataRoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.model.AppData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDataRepository @Inject constructor() //private val AppDataLocalDataSource: AppDataRoomDataSource)
{
//    fun getAppDataOffline(): Flow<List<AppDataEntity>> =
//        AppDataLocalDataSource.AppDataDao().getAppData();
            //.map { it.map(AppDataEntity::asExternalModel) }
}