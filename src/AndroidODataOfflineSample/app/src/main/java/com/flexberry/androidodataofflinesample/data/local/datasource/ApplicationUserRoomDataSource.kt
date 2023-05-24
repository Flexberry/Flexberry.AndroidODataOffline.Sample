package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.entities.ApplicationUserEntity
import javax.inject.Inject

class ApplicationUserRoomDataSource @Inject constructor(
    db: LocalDatabase
) : RoomDataSource<ApplicationUserEntity>(db.getApplicationUserDao(), ApplicationUserEntity.tableName) {

}