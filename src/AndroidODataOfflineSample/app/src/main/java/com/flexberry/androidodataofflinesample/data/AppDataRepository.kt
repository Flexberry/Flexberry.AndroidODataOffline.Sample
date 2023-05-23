package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.AppDataLocalDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import java.util.UUID
import javax.inject.Inject

class AppDataRepository @Inject constructor(
    @AppDataLocalDataSource private val localDataSource: LocalDataSource<AppDataEntity>
)
{
    /**
     * Установить флаг онлайн для данных приложения.
     *
     * @param isOnline Флаг онлайн.
     */
    fun setOnlineFlag(isOnline: Boolean) {
        val appDatas = localDataSource.readObjects()

        if (!appDatas.any()) {
            val appData = AppDataEntity(
                primarykey = UUID.randomUUID(),
                isOnline = isOnline
            )

            localDataSource.createObjects(appData);
        } else {
            val appData = AppDataEntity(
                primarykey = appDatas[0].primarykey,
                isOnline = isOnline
            )

            localDataSource.updateObjects(listOf(appData));
        }
    }
}