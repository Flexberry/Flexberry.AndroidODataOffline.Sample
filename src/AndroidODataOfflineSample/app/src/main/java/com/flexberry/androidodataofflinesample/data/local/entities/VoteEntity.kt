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
    val createTime: Timestamp? = null,

    @ColumnInfo(name = "Creator")
    val creator: String? = null,

    @ColumnInfo(name = "EditTime")
    val editTime: Timestamp? = null,

    @ColumnInfo(name = "Editor")
    val editor: String? = null,

    @ColumnInfo(name = "VoteType")
    var voteType: VoteType? = null,

    @ColumnInfo(name = "ApplicationUser")
    val applicationUserId: UUID? = null
)