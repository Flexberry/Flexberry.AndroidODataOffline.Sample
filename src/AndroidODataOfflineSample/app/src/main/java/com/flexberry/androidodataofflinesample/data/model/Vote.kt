package com.flexberry.androidodataofflinesample.data.model

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import java.sql.Timestamp
import java.util.UUID

/**
 * Представление внешнего уровня для Vote.
 */
data class Vote(
    val primarykey : UUID,
    val createTime: Timestamp,
    val creator: String,
    val editTime: Timestamp,
    val editor: String,
    val voteType: VoteType,
    val applicationUser: ApplicationUser
)