package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import com.flexberry.androidodataofflinesample.data.network.models.NetworkVote
import java.util.Date
import java.util.UUID

/**
 * Представление внешнего уровня для Vote.
 */
data class Vote(
    val primarykey : UUID,
    val createTime: Date? = null,
    val creator: String? = null,
    val editTime: Date? = null,
    val editor: String? = null,
    val voteType: VoteType? = null,
    val author: ApplicationUser
)

fun NetworkVote.asLocalModel() = Vote(
    primarykey = this.__PrimaryKey,
    createTime = this.CreateTime,
    creator = this.Creator,
    editTime = this.EditTime,
    editor = this.Editor,
    voteType = this.VoteType,
    author = this.Author.asLocalModel()
)