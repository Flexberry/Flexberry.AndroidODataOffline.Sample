package com.flexberry.androidodataofflinesample.data

import android.util.Log
import com.flexberry.androidodataofflinesample.data.di.AppDataLocalDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import java.util.UUID
import javax.inject.Inject

class AppDataRepository @Inject constructor(
    @AppDataLocalDataSource private val localDataSource: LocalDataSource<AppDataEntity>
)
{
    private var initialized = false

    /**
     * Первичная инициализация настроек.
     */
    fun initSettings(isOnline: Boolean = false) {
        if (!initialized) {
            val appDatas = localDataSource.readObjects()

            if (!appDatas.any()) {
                val appData = AppDataEntity(
                    primarykey = UUID.randomUUID(),
                    isOnline = isOnline
                )

                localDataSource.createObjects(appData);
            }

            initialized = true
        }
    }

    /**
     * Установить флаг онлайн для данных приложения.
     *
     * @param isOnline Флаг онлайн.
     */
    fun setOnlineFlag(isOnline: Boolean): Boolean {
        val appDatas = localDataSource.readObjects()

        if (!appDatas.any()) {
            Log.e("LocalDataSource read settings.", "Не могу получить настройки приложения")

            return false
        } else {
            val appData = AppDataEntity(
                primarykey = appDatas[0].primarykey,
                isOnline = isOnline
            )

            localDataSource.updateObjects(listOf(appData))
        }

        return true
    }
}