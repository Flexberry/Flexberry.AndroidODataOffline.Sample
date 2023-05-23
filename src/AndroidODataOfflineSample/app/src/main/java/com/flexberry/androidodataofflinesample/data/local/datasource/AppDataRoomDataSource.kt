package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import javax.inject.Inject

class AppDataRoomDataSource @Inject constructor(
    db: LocalDatabase
    ) : RoomDataSource<AppDataEntity>(db.getAppDataDao(), AppDataEntity.tableName) {

}