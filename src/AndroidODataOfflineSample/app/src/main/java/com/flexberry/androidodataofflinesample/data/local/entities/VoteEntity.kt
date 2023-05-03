package com.flexberry.androidodataofflinesample.data.local.entities

import androidx.room.*
import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.model.Vote
import java.sql.Timestamp
import java.util.UUID

@Entity(tableName = "EmberFlexberryDummyVotes")
data class VoteEntity(
    @PrimaryKey
    @ColumnInfo(name = "__PrimaryKey")
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
    val applicationUserId: UUID,

    @Embedded
    val applicationUser: ApplicationUserEntity
)

fun VoteEntity.asExternalModel() = Vote(
    primarykey = primarykey,
    createTime = createTime,
    creator = creator,
    editTime = editTime,
    editor = editor,
    voteType = voteType,
    applicationUser = applicationUser.asExternalModel()
)