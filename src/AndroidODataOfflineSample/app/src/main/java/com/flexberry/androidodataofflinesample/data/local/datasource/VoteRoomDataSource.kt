package com.flexberry.androidodataofflinesample.data.local.datasource

import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import com.flexberry.androidodataofflinesample.data.query.QuerySettings

class VoteRoomDataSource(db: LocalDatabase,
                         queryParams: QuerySettings
) : RoomDataSource<VoteEntity>(db, queryParams) {
    fun VoteDao() = db.getVoteDao()
}