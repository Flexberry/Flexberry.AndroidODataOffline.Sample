package com.flexberry.androidodataofflinesample.data

import com.flexberry.androidodataofflinesample.data.di.DetailLocalDatasource
import com.flexberry.androidodataofflinesample.data.di.MasterLocalDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import com.flexberry.androidodataofflinesample.data.local.interfaces.LocalDataSource
import javax.inject.Inject

/**
 * Репозиторий для тестовых данных
 */
class TestDataRoomRepository @Inject constructor(
    @DetailLocalDatasource private val detailLocalDataSource: LocalDataSource<DetailEntity>,
    @MasterLocalDataSource private val masterLocalDataSource: LocalDataSource<MasterEntity>,
) {
    fun initTestOfflineData() {
        val commonMaster = MasterEntity(name = "commonMaster")
        val d1 = DetailEntity(name = "Detail One", master = commonMaster)
        val d2 = DetailEntity(name = "Detail Two", master = commonMaster)
        val d3 = DetailEntity(name = "Detail Three", master = commonMaster)

        commonMaster.details = listOf(d1, d2, d3)

        masterLocalDataSource.createObjects(
            commonMaster,
            MasterEntity(name = "master1"),
            MasterEntity(name = "master2"),
            MasterEntity(name = "master3"),
        )
    }
}