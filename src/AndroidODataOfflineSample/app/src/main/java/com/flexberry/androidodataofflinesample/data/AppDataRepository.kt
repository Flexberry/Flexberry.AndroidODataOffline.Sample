package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.local.daos.AppDataDao
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.asExternalModel
import com.flexberry.androidodataofflinesample.data.model.AppData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataRepository(private val AppDataLocalDataSource: AppDataDao) {
    fun getAppDataOffline(): Flow<List<AppData>> =
        AppDataLocalDataSource.getAppData()
            .map { it.map(AppDataEntity::asExternalModel) }
}