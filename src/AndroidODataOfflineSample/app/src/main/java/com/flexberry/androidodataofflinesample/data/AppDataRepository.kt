package com.flexberry.androidodataofflinesample.data

import javax.inject.Inject

class AppDataRepository @Inject constructor()
    // TODO через конструктор репозитория будет внедряться local DataSources.
    // private val exampleLocalDataSource: ExampleLocalDataSource
{
    /*
    fun getAppDataOffline(): Flow<List<AppData>> =
        AppDataLocalDataSource.getAppData()
            .map { it.map(AppDataEntity::asExternalModel) }
}