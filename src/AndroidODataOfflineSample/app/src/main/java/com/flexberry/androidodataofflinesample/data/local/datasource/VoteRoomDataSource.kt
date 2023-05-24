package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSource
import com.flexberry.androidodataofflinesample.data.local.datasource.room.RoomDataSourceTypeManager
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import javax.inject.Inject

class VoteRoomDataSource @Inject constructor(
    typeManager: RoomDataSourceTypeManager
) : RoomDataSource<VoteEntity>(VoteEntity::class, typeManager) {

}