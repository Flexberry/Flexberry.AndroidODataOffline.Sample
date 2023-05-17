package com.flexberry.androidodataofflinesample.data.network.models

import com.flexberry.androidodataofflinesample.data.enums.VoteType
import java.sql.Timestamp
import java.util.UUID

/**
 * Network representation of [Vote]
 */
@Suppress("kotlin:S117")
data class NetworkVote(
    val __PrimaryKey : UUID,
    val CreateTime: Timestamp? = null,
    val Creator: String? = null,
    val EditTime: Timestamp? = null,
    val Editor: String? = null,
    val VoteType: VoteType? = null,
    val Author: NetworkApplicationUser
)