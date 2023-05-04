package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import java.sql.Timestamp
import java.util.UUID

@Entity(tableName = "Vote")
data class VoteEntity(
    @PrimaryKey
    @ColumnInfo(name = "__primaryKey")
    val primarykey : UUID,

    @ColumnInfo(name = "CreateTime")
    val createTime: Timestamp,

    @ColumnInfo(name = "Creator")
    val creator: String,

    @ColumnInfo(name = "EditTime")
    val editTime: Timestamp,

    @ColumnInfo(name = "Editor")
    val editor: String,

    @ColumnInfo(name = "VoteType")
    val voteType: VoteType,

    @ColumnInfo(name = "ApplicationUser")
    val applicationUserId: UUID
)
