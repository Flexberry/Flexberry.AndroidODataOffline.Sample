package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.entities.AppDataEntity
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import javax.inject.Inject

class VoteRoomDataSource @Inject constructor(
    db: LocalDatabase
) : RoomDataSource<VoteEntity>(db.getVoteDao(), VoteEntity.tableName) {

}