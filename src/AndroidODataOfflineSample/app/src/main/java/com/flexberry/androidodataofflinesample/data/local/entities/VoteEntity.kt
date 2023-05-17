package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.model.AppData
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import com.flexberry.androidodataofflinesample.data.model.Vote
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
    val voteType: VoteType? = null,

    @ColumnInfo(name = "ApplicationUser")
    val applicationUserId: UUID
)

/*
fun VoteEntity.asExternalModel() = Vote(
    primarykey = primarykey,
    createTime = createTime,
    creator = creator,
    editTime = editTime,
    editor = editor,
    voteType = voteType
)*/
