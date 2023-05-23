package com.flexberry.androidodataofflinesample.data.local.datasource

import androidx.room.Entity
import com.flexberry.androidodataofflinesample.data.local.entities.MasterEntity
import javax.inject.Inject

class MasterRoomDataSource @Inject constructor(
    db: LocalDatabase
) : RoomDataSource<MasterEntity>(db.getMasterDao(), MasterEntity.tableName) {

}