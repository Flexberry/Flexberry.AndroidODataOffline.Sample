package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.local.entities.VoteEntity
import java.sql.Timestamp
import java.util.UUID

/**
 * Network representation of [Vote]
 */
data class NetworkVote(
    val primarykey : UUID,
    val createTime: Timestamp,
    val creator: String,
    val editTime: Timestamp,
    val editor: String,
    val voteType: VoteType,
    val applicationUser: NetworkApplicationUser
)

fun NetworkVote.asEntity() = VoteEntity(
    primarykey = primarykey,
    createTime = createTime,
    creator = creator,
    editTime = editTime,
    editor = editor,
    voteType = voteType,
    applicationUserId = applicationUser.primarykey,
    applicationUser = applicationUser.asEntity()
)