package com.flexberry.androidodataofflinesample.data.local.datasource

import androidx.room.Entity
import com.flexberry.androidodataofflinesample.data.local.entities.DetailEntity
import javax.inject.Inject

class DetailRoomDataSource @Inject constructor(
    db: LocalDatabase
) : RoomDataSource<DetailEntity>(db.getDetailDao(), DetailEntity.tableName) {

}